package com.itproger.vacuummovies.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

public class UploadActivity extends AppCompatActivity {
    EditText filmName, filmYear, filmDirector;
    Button addFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        filmName = findViewById(R.id.film_name);
        filmYear = findViewById(R.id.film_year);
        filmDirector = findViewById(R.id.film_director);
        addFilm = findViewById(R.id.add_film);

        addFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });
    }

    public void uploadData() {
        String name = filmName.getText().toString();
        String year = filmYear.getText().toString();
        String director = filmDirector.getText().toString();

        Film film = new Film(name, year, director);

        FirebaseDatabase.getInstance().getReference("Films").child(name).setValue(film).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Фильм успешно добавлен", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}