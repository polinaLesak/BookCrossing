package com.example.bookcrossing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookcrossing.databinding.ActivityMainBinding;
import com.example.bookcrossing.databinding.ActivityProfileBinding;
import com.example.bookcrossing.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    Button btnRegister, btnLogin;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    ConstraintLayout root;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);


        root = findViewById(R.id.root_element);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");


        if (auth.getUid()!= null){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        }
        btnRegister.setOnClickListener(view -> showRegisterWindow());
        btnLogin.setOnClickListener(view -> showLogInWindow());
    }

    private void showLogInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign In");
        dialog.setMessage("Enter email and password");

        LayoutInflater inflater = LayoutInflater.from(this);
        View logIn_window = inflater.inflate(R.layout.signin_window, null);
        dialog.setView(logIn_window);

        final MaterialEditText email = logIn_window.findViewById(R.id.email);
        final MaterialEditText password = logIn_window.findViewById(R.id.password);


        dialog.setNegativeButton("Cansel", (dialogInterface, i) -> dialogInterface.dismiss());

        //ввод корректных данных
        dialog.setPositiveButton("Done", (dialogInterface, i) -> {

            if (TextUtils.isEmpty(email.getText().toString())) {
                Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (password.getText().toString().length() < 5) {
                Snackbar.make(root, "Enter your password more 5", Snackbar.LENGTH_SHORT).show();
                return;
            }
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {

                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        finish();
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(root, "Wrong email or password", Snackbar.LENGTH_LONG).show();
                        }
                    });
        });

        dialog.show();
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registration");
        dialog.setMessage("Enter the registration details");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        final MaterialEditText name = register_window.findViewById(R.id.name);

        final MaterialEditText email = register_window.findViewById(R.id.email);
        final MaterialEditText password = register_window.findViewById(R.id.password);
        final MaterialEditText phone = register_window.findViewById(R.id.phone);

        dialog.setNegativeButton("Cansel", (dialogInterface, i) -> dialogInterface.dismiss());

        //ввод корректных данных
        dialog.setPositiveButton("Add", (dialogInterface, i) -> {
            if (TextUtils.isEmpty(name.getText().toString())) {
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            String em = email.getText().toString();
            int pos = em.length() - em.indexOf("@gmail.com");
            if (TextUtils.isEmpty(email.getText().toString())||pos !=10) {
                Toast.makeText(this, "Enter your email consists @gmail.com", Toast.LENGTH_SHORT).show();

                return;
            }
            String  ph = phone.getText().toString();
            char first = ph.charAt(0);
            if (ph.length()!=13||first!='+') {
                Toast.makeText(this, "Enter right phone", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.getText().toString().length() < 5) {
                Toast.makeText(this, "Enter your password more 5", Toast.LENGTH_SHORT).show();
                return;
            }
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User(name.getText().toString(),
                                email.getText().toString(), password.getText().toString(), phone.getText().toString());
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnSuccessListener(aVoid -> {
                                    Snackbar.make(root, "User added!", Snackbar.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                    finish();
                                }).addOnFailureListener(e -> Snackbar.make(root, "Registration error"+ e.getMessage(),
                                        Snackbar.LENGTH_SHORT).show());
                    });
        });

        dialog.show();


    }
}