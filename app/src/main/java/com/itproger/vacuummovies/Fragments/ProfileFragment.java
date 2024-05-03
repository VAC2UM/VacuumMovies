package com.itproger.vacuummovies.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.itproger.vacuummovies.Activities.LoginActivity;
import com.itproger.vacuummovies.Activities.UploadActivity;
import com.itproger.vacuummovies.R;

public class ProfileFragment extends Fragment {

//    Button exit;
    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

//        exit = rootView.findViewById(R.id.exit);
        fab = rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadActivity.class);
                startActivity(intent);
            }
        });
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });
        return rootView;
    }
}