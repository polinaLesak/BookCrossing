package com.example.bookcrossing.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookcrossing.ChatDetailActivity;
import com.example.bookcrossing.OtherProfileActivity;
import com.example.bookcrossing.R;
import com.example.bookcrossing.models.Book;
import com.example.bookcrossing.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    ArrayList<Book> books;
    Context context;

    public BookAdapter(ArrayList<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_book, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book book = books.get(position);
        //User user = users.get(position);
        holder.bookName.setText(book.getBookName());
        holder.author.setText(book.getAuthor());

        if (book.getuId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context).setTitle("Delete Book")
                            .setMessage("Are you shure you want to delete book")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    database.getReference().child("Books").child(book.getBookId()).removeValue();


                                }

                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                    return false;
                }
            });
        }
        if (!book.getuId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OtherProfileActivity.class);
                    intent.putExtra("uId", book.getuId());
                    intent.putExtra("bookPic", book.getBookPic());
                    intent.putExtra("bookId", book.getBookId());
                    intent.putExtra("bookName", book.getBookName());
                    intent.putExtra("author", book.getAuthor());
                    context.startActivity(intent);
                }
            });
        }

    }
  public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bookName, author;
      public ViewHolder(@NonNull View itemView) {
          super(itemView);
          bookName = itemView.findViewById(R.id.bookNameList);
          author = itemView.findViewById(R.id.authorList);
      }

  }


    @Override
    public int getItemCount() {
        return books.size();
    }
}
