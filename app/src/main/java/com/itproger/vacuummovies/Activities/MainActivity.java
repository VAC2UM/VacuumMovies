package com.itproger.vacuummovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Fragments.FilmsFragment;
import com.itproger.vacuummovies.Fragments.ProfileFragment;
import com.itproger.vacuummovies.Fragments.SettingsFragment;
import com.itproger.vacuummovies.Fragments.SuperProfileFragment;
import com.itproger.vacuummovies.R;
import com.itproger.vacuummovies.User;
import com.itproger.vacuummovies.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    //    Использование binding в Android дает доступ к представлениям (views) макета (layout) напрямую
//    из кода без необходимости вызова метода findViewById().
//    Он упрощает работу с представлениями и делает код более чистым и лаконичным.
    ActivityMainBinding binding;
    boolean isSup;

    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FilmsFragment());

        db = FirebaseDatabase.getInstance();

        binding.bottomNavigatiomView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filmsList) {
                replaceFragment(new FilmsFragment());
            } else if (itemId == R.id.profile) {
                userSuperUser();
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

    private void userSuperUser() {
        Intent intent = getIntent();

        String nameUser = intent.getStringExtra(Constant.USERNAME);
        String emailUser = intent.getStringExtra(Constant.EMAIL);
        String passwordUser = intent.getStringExtra(Constant.PASSWORD);
        boolean isSuperUser = intent.getBooleanExtra(Constant.SUPERUSER, false);

        // Отображение определенного фрагмента в зависимости от роли пользователя
        if (isSuperUser) {
            replaceFragment(new SuperProfileFragment());
        } else {
            replaceFragment(new ProfileFragment());
        }
    }
}