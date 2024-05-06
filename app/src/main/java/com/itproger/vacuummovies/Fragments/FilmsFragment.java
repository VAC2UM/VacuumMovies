package com.itproger.vacuummovies.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.vacuummovies.Adapter.MyAdapter;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

import java.util.ArrayList;

public class FilmsFragment extends Fragment {

    GridView gridView;
    ArrayList<Film> filmsList;
    FirebaseDatabase db;
    SearchView searchView;

    AlertDialog.Builder builder;
    AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_films, container, false);

        builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        dialog = builder.create();
        dialog.show();

        gridView = rootView.findViewById(R.id.gridView);
        searchView = rootView.findViewById(R.id.search);

        filmsList = new ArrayList<>();

        db = FirebaseDatabase.getInstance();

        loadDatainGridView();
        return rootView;
    }

    private void loadDatainGridView() {
        db.getReference(Constant.FILMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Film film = snapshot.getValue(Film.class);
                        filmsList.add(film);
                    }
                    MyAdapter adapter = new MyAdapter(getContext(), filmsList);
                    gridView.setAdapter(adapter);

                    // Закрываем диалоговое окно загрузки после установки адаптера
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    // В случае отсутствия данных также закрываем диалоговое окно
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                // В случае ошибки также закрываем диалоговое окно
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searhList(newText);
                return false;
            }
        });
    }

    public void searhList(String text) {
        ArrayList<Film> searchList = new ArrayList<>();
        for (Film film : filmsList) {
            if(film.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(film);
            }
        }
        MyAdapter adapter = new MyAdapter(getContext(), searchList);
        gridView.setAdapter(adapter);
    }
}