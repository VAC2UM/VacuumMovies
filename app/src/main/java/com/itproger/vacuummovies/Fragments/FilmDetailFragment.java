package com.itproger.vacuummovies.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

public class FilmDetailFragment extends Fragment {
    TextView filmName, directorTextView, yearTextView, descriptionTextView;
    ImageView detailImage, youtubeIcon;
    FloatingActionButton deleteBtn, editBtn;
    FloatingActionMenu floatList;
    String key = "";
    String imageUrl = "";
    String trailer;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film_detail, container, false);

        filmName = rootView.findViewById(R.id.detailTitle);
        directorTextView = rootView.findViewById(R.id.detailDirector);
        yearTextView = rootView.findViewById(R.id.detailYear);
        descriptionTextView = rootView.findViewById(R.id.detailDescription);
        detailImage = rootView.findViewById(R.id.detailImage);
        youtubeIcon = rootView.findViewById(R.id.youtubeButton);
        floatList = rootView.findViewById(R.id.floating_menu);
        deleteBtn = rootView.findViewById(R.id.deleteButton);
        editBtn = rootView.findViewById(R.id.editButton);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean isSuperUser = sharedPref.getBoolean(getString(R.string.saved_super_user_key), false);

        if (isSuperUser) {
            floatList.setVisibility(View.VISIBLE);
        } else {
            floatList.setVisibility(View.GONE);
        }

        // Получаем данные о фильме из Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String fName = bundle.getString(Constant.NAME);
            key = bundle.getString(Constant.KEY);
            imageUrl = bundle.getString(Constant.DATAIMAGE);
            trailer = bundle.getString(Constant.TRAILERLINK);
            Glide.with(getContext()).load(bundle.getString(Constant.DATAIMAGE)).into(detailImage);
            loadFilmData(fName);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.FILMS);
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FilmsFragment filmsFragment = new FilmsFragment();
                        reference.child(key).removeValue();
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

                        ((AppCompatActivity) getContext()).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout, filmsFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateFragment updateFragment = new UpdateFragment();

                Bundle bundle = new Bundle();
                bundle.putString(Constant.NAME, filmName.getText().toString());
                bundle.putString(Constant.DATAIMAGE, imageUrl);
                bundle.putString(Constant.YEAR, yearTextView.getText().toString());
                bundle.putString(Constant.DIRECTOR, directorTextView.getText().toString());
                bundle.putString(Constant.DESCRIPTION, descriptionTextView.getText().toString());
                bundle.putString(Constant.TRAILERLINK, trailer);
                bundle.putString(Constant.KEY, key);
                updateFragment.setArguments(bundle);

                ((AppCompatActivity) getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, updateFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        youtubeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trailer != null && !trailer.isEmpty()) {
                    goToURL(trailer);
                } else {
                    Toast.makeText(getContext(), "Трейлера еще нет", Toast.LENGTH_SHORT).show();
                }            }
        });

        return rootView;
    }

    private void goToURL(String link) {
        Uri uri = Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
                    directorTextView.setText(film.getDirector());
                    yearTextView.setText(film.getYear());
                    descriptionTextView.setText(film.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
