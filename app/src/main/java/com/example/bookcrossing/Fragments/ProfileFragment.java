package com.example.bookcrossing.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.bookcrossing.Adapters.BookAdapter;
import com.example.bookcrossing.Adapters.UserAdapter;
import com.example.bookcrossing.MainActivity;
import com.example.bookcrossing.ProfileActivity;
import com.example.bookcrossing.databinding.FragmentProfileBinding;
import com.example.bookcrossing.models.Book;
import com.example.bookcrossing.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
    }

    FragmentProfileBinding binding;
    ArrayList<User> listU = new ArrayList<>();
    ArrayList<Book> listB = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getActivity());

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        UserAdapter adapter = new UserAdapter(listU, getContext());

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://bookcrossing-f84fd.appspot.com");


        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            user.setUserId(snapshot.getKey());

                            binding.txtPhone.setText(user.getPhone());
                            binding.txtEmail.setText(user.getEmail());
                            binding.txtUserName.setText(user.getName());
                            binding.txtPassword.setText(user.getPassword());

                            Picasso.get().load(user.getProfilePic()).into(binding.profileImage);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = binding.txtEmail.getText().toString();
                int pos = em.length() - em.indexOf("@gmail.com");
                if ((!binding.txtUserName.getText().toString().equals("") &&
                        !binding.txtEmail.getText().toString().equals("") &&
                        !binding.txtPhone.getText().toString().equals("")&&!binding.txtPassword.getText().toString().equals(""))||pos==10){
                    String userName = binding.txtUserName.getText().toString();
                    String email = binding.txtEmail.getText().toString();
                    String phone = binding.txtPhone.getText().toString();
                    String password = binding.txtPassword.getText().toString();


                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("name", userName);
                    obj.put("email", email);
                    obj.put("phone", phone);
                    obj.put("password", password);



                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);

                    Toast toast = Toast.makeText(getActivity(),
                            "Profile updated", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
            }
        });



        binding.saveBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.txtAuthor.getText().toString().equals("") &&
                        !binding.txtBookName.getText().toString().equals("")) {
                    String autho = binding.txtAuthor.getText().toString();
                    String bookNam = binding.txtBookName.getText().toString();
                    String uId = FirebaseAuth.getInstance().getUid();



                    final Book book = new Book(bookNam, autho,uId);
                    database.getReference().child("Books").push().setValue(book);
                    Toast.makeText(getActivity(), "Book is added", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();

                }
            }
        });

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();

        BookAdapter adapterB = new BookAdapter(listB, getContext());
        binding.bookRecyclerView.setAdapter(adapterB);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

         binding.bookRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listB.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Book book = snapshot1.getValue(Book.class);
                    book.setBookId(snapshot1.getKey());
                    //book.setBookName(binding);
                    if (book.getuId().equals(FirebaseAuth.getInstance().getUid())) {
                        listB.add(book);

                    }
                }
                for(int i=0; i<listB.size(); i++){
                    adapterB.notifyItemChanged(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri filePath = data.getData();
        if (requestCode == 71
                && data != null && data.getData() != null) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                binding.profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            StorageReference ref = storageReference.child("profilePic").child(FirebaseAuth.getInstance().getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                            .child("profilePic").setValue(String.valueOf(task.getResult()));
                                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}