package com.itproger.vacuummovies.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itproger.vacuummovies.Adapter.MyAdapter;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

import java.util.ArrayList;

public class FilmsFragment extends Fragment {

    GridView gridView;
    ArrayList<Film> filmsList;
    FirebaseDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_films, container, false);

        gridView = rootView.findViewById(R.id.gridView);
        filmsList = new ArrayList<>();

        db = FirebaseDatabase.getInstance();

        loadDatainGridView();
        return rootView;
    }

    private void loadDatainGridView() {
        db.getReference("Films").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Film film = snapshot.getValue(Film.class);
                        filmsList.add(film);
                    }
                    MyAdapter adapter = new MyAdapter(getContext(), filmsList);
                    gridView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}