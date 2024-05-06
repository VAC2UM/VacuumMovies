package com.itproger.vacuummovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.R;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.security.MessageDigest;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextLogin, editTextPassword;
    Button buttonLogin;
    //    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;


//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null && currentUser.isEmailVerified()) {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mAuth = FirebaseAuth.getInstance();
        editTextLogin = findViewById(R.id.loginName);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {

                } else {
                    checkUser();
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public Boolean validateUsername() {
        String val = editTextLogin.getText().toString();
        if (val.isEmpty()) {
            editTextLogin.setError("Поле имени не может быть пустым");
            return false;
        } else {
            editTextLogin.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = editTextPassword.getText().toString();
        if (val.isEmpty()) {
            editTextPassword.setError("Поле пароля не может быть пустым");
            return false;
        } else {
            editTextPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUserName = editTextLogin.getText().toString().trim();
        String userPassword = hashPassword(editTextPassword.getText().toString().trim());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constant.USERS);
        Query checkUserDB = reference.orderByChild("username").equalTo(userUserName);

        checkUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editTextLogin.setError(null);
                    String passwordFromDB = snapshot.child(userUserName).child("password").getValue(String.class);
                    Toast.makeText(LoginActivity.this, snapshot.child(userUserName).child("password").getValue(String.class), Toast.LENGTH_SHORT).show();
                    if (Objects.equals(passwordFromDB, userPassword)) {
                        editTextLogin.setError(null);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        editTextPassword.setError("Invalid Credentials");
                        editTextPassword.requestFocus(); // узнать что это
                    }
                } else {
                    editTextLogin.setError("User does not exist");
                    editTextPassword.requestFocus(); // узнать что это
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}