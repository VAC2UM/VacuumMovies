package com.itproger.vacuummovies.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadFragment extends Fragment {
    ImageView uploadImage;
    TextInputEditText filmName, filmYear, filmDirector, filmDescription;
    Button saveButton;
    String imageURL;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);

        uploadImage = rootView.findViewById(R.id.uploadImage);
        filmName = rootView.findViewById(R.id.film_name);
        filmYear = rootView.findViewById(R.id.film_year);
        filmDirector = rootView.findViewById(R.id.film_director);
        filmDescription = rootView.findViewById(R.id.film_description);
        saveButton = rootView.findViewById(R.id.save_film);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(getContext(), "Постер не выбран", Toast.LENGTH_SHORT);
                        }
                    }
                }
        );
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        return rootView;
    }

    public void saveData() {
        String name = filmName.getText().toString();
        String year = filmYear.getText().toString();
        String director = filmDirector.getText().toString();
        String description = filmDescription.getText().toString();

        if (TextUtils.isEmpty(name)) {
            filmName.setError("Поле не может быть пустым");
            filmName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(year)) {
            filmYear.setError("Поле не может быть пустым");
            filmYear.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(director)) {
            filmDirector.setError("Поле не может быть пустым");
            filmDirector.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            filmDirector.setError("Поле не может быть пустым");
            filmDirector.requestFocus();
            return;
        }
        if (name.contains("\n")) {
            filmName.setError("Поле имеет запрещенный символ ('\\n')");
            filmName.requestFocus();
            return;
        }
        if (year.contains("\n")) {
            filmYear.setError("Поле имеет запрещенный символ ('\\n')");
            filmYear.requestFocus();
            return;
        }
        if (director.contains("\n")) {
            filmDirector.setError("Поле имеет запрещенный символ ('\\n')");
            filmDirector.requestFocus();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.saving_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void uploadData() {
        String name = filmName.getText().toString();
        String year = filmYear.getText().toString();
        String director = filmDirector.getText().toString();
        String description = filmDescription.getText().toString();

        Film film = new Film(name, year, director, imageURL, description);

        FirebaseDatabase.getInstance().getReference("Films").child(name).setValue(film).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Фильм успешно добавлен", Toast.LENGTH_SHORT).show();
                    FilmsFragment filmsFragment = new FilmsFragment();

                    ((AppCompatActivity) getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, filmsFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}