package com.itproger.vacuummovies.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

public class FilmDetailFragment extends Fragment {
    TextView filmName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film_detail, container, false);

        filmName = rootView.findViewById(R.id.textViewFilmName);

        // Получаем данные о фильме из Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String filmName = bundle.getString("name");
            loadFilmData(filmName);
        }

        return rootView;
    }

    private void loadFilmData(String name) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.FILMS).child(name);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Film film = dataSnapshot.getValue(Film.class);

                    filmName.setText(film.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
