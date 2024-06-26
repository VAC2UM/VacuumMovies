package com.itproger.vacuummovies.Activities;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.FirebaseDatabase;
import com.itproger.vacuummovies.Fragments.FilmsFragment;
import com.itproger.vacuummovies.Fragments.InfoFragment;
import com.itproger.vacuummovies.Fragments.ProfileFragment;
import com.itproger.vacuummovies.R;
import com.itproger.vacuummovies.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    //    Использование binding в Android дает доступ к представлениям (views) макета (layout) напрямую
//    из кода без необходимости вызова метода findViewById().
//    Он упрощает работу с представлениями и делает код более чистым и лаконичным.
    ActivityMainBinding binding;
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
                replaceFragment(new ProfileFragment());
            } else if (itemId == R.id.info) {
                replaceFragment(new InfoFragment());
            }
            return true;
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Обработчик нажатия кнопки "Назад"
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