package com.itproger.vacuummovies.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.FirebaseDatabase;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Fragments.FilmsFragment;
import com.itproger.vacuummovies.Fragments.ProfileFragment;
import com.itproger.vacuummovies.Fragments.SettingsFragment;
import com.itproger.vacuummovies.Fragments.SuperProfileFragment;
import com.itproger.vacuummovies.R;
import com.itproger.vacuummovies.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    //    Использование binding в Android дает доступ к представлениям (views) макета (layout) напрямую
//    из кода без необходимости вызова метода findViewById().
//    Он упрощает работу с представлениями и делает код более чистым и лаконичным.
    ActivityMainBinding binding;
    boolean isSup;

    FirebaseDatabase db;
    SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FilmsFragment());

        db = FirebaseDatabase.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();

        String nameUser = intent.getStringExtra(Constant.USERNAME);
        String emailUser = intent.getStringExtra(Constant.EMAIL);
        String passwordUser = intent.getStringExtra(Constant.PASSWORD);
        boolean isSuperUser = intent.getBooleanExtra(Constant.SUPERUSER, false);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_super_user_key), isSuperUser);
        editor.apply();

        binding.bottomNavigatiomView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filmsList) {
                replaceFragment(new FilmsFragment());
            } else if (itemId == R.id.profile) {
                if (isSuperUser) {
                    replaceFragment(new SuperProfileFragment());
                } else {
                    replaceFragment(new ProfileFragment());
                }
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    // Замена одного фрагмента на другой
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}