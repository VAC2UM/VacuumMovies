package com.itproger.vacuummovies;

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

//        binding.addFilm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name = binding.filmName.getText().toString();
//                date = binding.filmDate.getText().toString();
//                director = binding.filmDirector.getText().toString();
//
//                if (!name.isEmpty() && !director.isEmpty() && !date.isEmpty()) {
//                    Films films = new Films(name, date, director);
//                    db = FirebaseDatabase.getInstance();
//                    reference = db.getReference("Films");
//                    reference.child(name).setValue(films).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            binding.filmName.setText("");
//                            binding.filmDate.setText("");
//                            binding.filmDirector.setText("");
//                            Toast.makeText(UploadActivity.this, "Фильм успешно добавлен", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//--------------------------------------------
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