package com.example.bookcrossing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookcrossing.databinding.ActivityOtherProfileBinding;
import com.example.bookcrossing.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OtherProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityOtherProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

       // getSupportActionBar().hide();
        binding = ActivityOtherProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String bookName = getIntent().getStringExtra("bookName");
        String ownerId = getIntent().getStringExtra("uId");
        String bookId = getIntent().getStringExtra("bookId");
        String bookPic = getIntent().getStringExtra("bookPic");
        String author = getIntent().getStringExtra("author");

        Picasso.get().load(bookPic).placeholder(R.drawable.book).into(binding.bookPic);

        final User[] owner = new User[1];

        database.getReference().child("Users").child(ownerId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                owner[0] = snapshot.getValue(User.class);
                                owner[0].setUserId(ownerId);
                                Picasso.get().load(owner[0].getProfilePic()).into(binding.profileImage);

                                binding.userEmail.setText(owner[0].getEmail());
                                binding.userName.setText(owner[0].getName());
                                binding.userPhone.setText(owner[0].getPhone());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        binding.bookName.setText(bookName);
        binding.author.setText(author);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.chatPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherProfileActivity.this, ChatDetailActivity.class);

                intent.putExtra("userId", ownerId);

                intent.putExtra("profilePic", owner[0].getProfilePic());
                intent.putExtra("userName", owner[0].getName());
                startActivity(intent);
            }
        });
    }
}