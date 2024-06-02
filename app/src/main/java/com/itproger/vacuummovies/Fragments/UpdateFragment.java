package com.itproger.vacuummovies.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

import java.util.Objects;

public class UpdateFragment extends Fragment {
    ImageView updateImage;
    Button updateButton;
    TextInputEditText updateFilmName, updateFilmYear, updateFilmDirector, updateFilmDescription, updateLink;
    String name, year, director, description, trailer;
    String oldName;
    String imageURL;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update, container, false);

        updateButton = rootView.findViewById(R.id.update_film);
        updateFilmName = rootView.findViewById(R.id.film_name);
        updateImage = rootView.findViewById(R.id.update_image);
        updateFilmYear = rootView.findViewById(R.id.film_year);
        updateFilmDirector = rootView.findViewById(R.id.film_director);
        updateFilmDescription = rootView.findViewById(R.id.film_description);
        updateLink = rootView.findViewById(R.id.film_trailerLink);

        updateFilmName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        updateFilmYear.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        updateFilmDirector.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        updateFilmDescription.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});
        updateLink.setFilters(new InputFilter[]{new InputFilter.LengthFilter(150)});
        updateFilmName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    updateFilmYear.requestFocus();
                    updateFilmYear.setSelection(updateFilmYear.getText().length());
                    return true;
                }
                return false;
            }
        });
        updateFilmYear.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    updateFilmDirector.requestFocus();
                    updateFilmDirector.setSelection(updateFilmDirector.getText().length());
                    return true;
                }
                return false;
            }
        });
        updateFilmDirector.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    updateFilmDescription.requestFocus();
                    updateFilmDescription.setSelection(updateFilmDescription.getText().length());
                    return true;
                }
                return false;
            }
        });
        updateFilmDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    updateLink.requestFocus();
                    updateLink.setSelection(updateLink.getText().length());
                    return true;
                }
                return false;
            }
        });
        updateLink.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    saveData();
                    return true;
                }
                return false;
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        } else {
                            Toast.makeText(getContext(), "Постер не выбран", Toast.LENGTH_SHORT);
                        }
                    }
                }
        );
        Bundle bundle = getArguments();
        if (bundle != null) {
            Glide.with(getContext()).load(bundle.getString(Constant.DATAIMAGE)).into(updateImage);
            updateFilmName.setText(bundle.getString(Constant.NAME));
            oldName = bundle.getString(Constant.NAME);
            updateFilmYear.setText(bundle.getString(Constant.YEAR));
            updateFilmDirector.setText(bundle.getString(Constant.DIRECTOR));
            updateFilmDescription.setText(bundle.getString(Constant.DESCRIPTION));
            updateLink.setText(bundle.getString(Constant.TRAILERLINK));
            key = bundle.getString(Constant.KEY);
            oldImageURL = bundle.getString(Constant.DATAIMAGE);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.FILMS).child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        return rootView;
    }

    public void saveData() {
        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());

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
                    updateData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } else {
            updateDataWithoutImage();
        }
    }

    public void updateData() {
        name = updateFilmName.getText().toString();
        year = updateFilmYear.getText().toString().trim();
        director = updateFilmDirector.getText().toString();
        description = updateFilmDescription.getText().toString();
        trailer = updateLink.getText().toString();

        if (TextUtils.isEmpty(name)) {
            updateFilmName.setError("Поле не может быть пустым");
            updateFilmName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(year)) {
            updateFilmYear.setError("Поле не может быть пустым");
            updateFilmYear.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(director)) {
            updateFilmDirector.setError("Поле не может быть пустым");
            updateFilmDirector.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            updateFilmDescription.setError("Поле не может быть пустым");
            updateFilmDescription.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(trailer)) {
            updateLink.setError("Поле не может быть пустым");
            updateLink.requestFocus();
            return;
        }
        if (name.contains("\n")) {
            updateFilmName.setError("Поле имеет запрещенный символ ('\\n')");
            updateFilmName.requestFocus();
            return;
        }
        if (year.contains("\n")) {
            updateFilmYear.setError("Поле имеет запрещенный символ ('\\n')");
            updateFilmYear.requestFocus();
            return;
        }
        if (director.contains("\n")) {
            updateFilmDirector.setError("Поле имеет запрещенный символ ('\\n')");
            updateFilmDirector.requestFocus();
            return;
        }
        if (trailer.contains("\n")) {
            updateLink.setError("Поле имеет запрещенный символ ('\\n')");
            updateLink.requestFocus();
            return;
        }

        Film film = new Film(name, year, director, imageURL, description, trailer);

        databaseReference.setValue(film).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(getContext(), "Обновлен", Toast.LENGTH_SHORT).show();

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

    public void updateDataWithoutImage() {
        final String newName = updateFilmName.getText().toString();
        name = newName;
        year = updateFilmYear.getText().toString().trim();
        director = updateFilmDirector.getText().toString();
        description = updateFilmDescription.getText().toString();
        trailer = updateLink.getText().toString();

        if (TextUtils.isEmpty(name)) {
            updateFilmName.setError("Поле не может быть пустым");
            updateFilmName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(year)) {
            updateFilmYear.setError("Поле не может быть пустым");
            updateFilmYear.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(director)) {
            updateFilmDirector.setError("Поле не может быть пустым");
            updateFilmDirector.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            updateFilmDescription.setError("Поле не может быть пустым");
            updateFilmDescription.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(trailer)) {
            updateLink.setError("Поле не может быть пустым");
            updateLink.requestFocus();
            return;
        }
        if (name.contains("\n")) {
            updateFilmName.setError("Поле имеет запрещенный символ ('\\n')");
            updateFilmName.requestFocus();
            return;
        }
        if (year.contains("\n")) {
            updateFilmYear.setError("Поле имеет запрещенный символ ('\\n')");
            updateFilmYear.requestFocus();
            return;
        }
        if (director.contains("\n")) {
            updateFilmDirector.setError("Поле имеет запрещенный символ ('\\n')");
            updateFilmDirector.requestFocus();
            return;
        }
        if (trailer.contains("\n")) {
            updateLink.setError("Поле имеет запрещенный символ ('\\n')");
            updateLink.requestFocus();
            return;
        }
        Film film = new Film(name, year, director, oldImageURL, description, trailer);

        databaseReference.setValue(film).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!Objects.equals(oldName, newName)) {
                        // Если изменилось, обновляем ключ узла
                        databaseReference.getParent().child(newName).setValue(film);
                        // Удаляем старый узел
                        databaseReference.getParent().child(oldName).removeValue();
                    }
                    Toast.makeText(getContext(), "Обновлено", Toast.LENGTH_SHORT).show();

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