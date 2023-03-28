package com.example.bookcrossing.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookcrossing.Adapters.BookAdapter;
import com.example.bookcrossing.Adapters.UserAdapter;
import com.example.bookcrossing.R;
import com.example.bookcrossing.databinding.FragmentBooksBinding;
import com.example.bookcrossing.databinding.FragmentChatsBinding;
import com.example.bookcrossing.models.Book;
import com.example.bookcrossing.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class BooksFragment extends Fragment {

    FragmentBooksBinding binding;
    ArrayList<Book> list = new ArrayList<>();
    FirebaseDatabase database;


    public BooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBooksBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();

        BookAdapter adapter = new BookAdapter(list, getContext());
        binding.bookRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.bookRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Book book = snapshot1.getValue(Book.class);
                    book.setBookId(snapshot1.getKey());
                    //book.setBookName(binding);
                    if (!book.getuId().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(book);
                    }
                }
               for(int i=0; i<list.size(); i++){
                    adapter.notifyItemChanged(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}