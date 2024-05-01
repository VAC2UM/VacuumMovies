package com.itproger.vacuummovies.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.vacuummovies.Fragments.FilmsFragment;
import com.itproger.vacuummovies.Fragments.ProfileFragment;
import com.itproger.vacuummovies.Fragments.SuperProfileFragment;
import com.itproger.vacuummovies.R;
import com.itproger.vacuummovies.Fragments.SettingsFragment;
import com.itproger.vacuummovies.User;
import com.itproger.vacuummovies.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FilmsFragment());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        binding.bottomNavigatiomView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filmsList) {
                replaceFragment(new FilmsFragment());
            } else if (itemId == R.id.profile) {
                // TODO: Сделать проверку на суперпользователя
                replaceFragment(new ProfileFragment());
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