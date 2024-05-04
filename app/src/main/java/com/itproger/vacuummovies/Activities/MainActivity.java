package com.itproger.vacuummovies.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.itproger.vacuummovies.Fragments.FilmsFragment;
import com.itproger.vacuummovies.Fragments.ProfileFragment;
import com.itproger.vacuummovies.Fragments.SettingsFragment;
import com.itproger.vacuummovies.R;
import com.itproger.vacuummovies.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

//    FirebaseDatabase database;
//    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FilmsFragment());

//        database = FirebaseDatabase.getInstance();
//        ref = database.getReference("Users");

        binding.bottomNavigatiomView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filmsList) {
                replaceFragment(new FilmsFragment());
            } else if (itemId == R.id.profile) {
                // TODO: Сделать проверку на суперпользователя
                replaceFragment(new ProfileFragment());
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        User user = snapshot.getValue(User.class);
//                        Toast.makeText(MainActivity.this, "Фильм: " + user.getEmail(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}