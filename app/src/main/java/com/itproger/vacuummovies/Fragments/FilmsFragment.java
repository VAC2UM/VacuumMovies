package com.itproger.vacuummovies.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproger.vacuummovies.Adapter.MyAdapter;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

import java.util.ArrayList;
import java.util.List;

public class FilmsFragment extends Fragment {
    List<Film> filmList;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    SearchView searchView;
    private MyAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_films, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        int numColons = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numColons);
        recyclerView.setLayoutManager(gridLayoutManager);

        builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        dialog = builder.create();
        dialog.show();

        filmList = new ArrayList<>();

        // TODO: попробовать создать другой адаптер в зависимости от роли пользователя
        adapter = new MyAdapter(getContext(), filmList);
        recyclerView.setAdapter(adapter);
        searchView = rootView.findViewById(R.id.search);
        searchView.clearFocus();

        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.FILMS);
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filmList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Film film = itemSnapshot.getValue(Film.class);
                    film.setKey(itemSnapshot.getKey());
                    filmList.add(film);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        return rootView;
    }

    public void searhList(String text) {
        ArrayList<Film> searchList = new ArrayList<>();
        for (Film film : filmList) {
            if(film.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(film);
            }
        }
        adapter.searhFilmList(searchList);
    }
}