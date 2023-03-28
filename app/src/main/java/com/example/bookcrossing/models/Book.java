package com.example.bookcrossing.models;

public class Book {
    String bookName, author, uId, bookId, bookPic, profilePic;

    public Book(String bookName, String author, String uId) {
        this.bookName = bookName;
        this.author = author;
        this.uId = uId;
    }

    public String getBookPic() {
        return bookPic;
    }

    public Book(String bookName, String author, String uId, String profilePic) {
        this.bookName = bookName;
        this.author = author;
        this.uId = uId;
        this.profilePic = profilePic;
    }

    public Book(String bookName, String author, String uId, String bookPic, String profilePic) {
        this.bookName = bookName;
        this.author = author;
        this.uId = uId;
        this.bookPic = bookPic;
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getBookId() {
        return bookId;
    }

    public Book(String bookName, String uId) {
        this.bookName = bookName;
        this.uId = uId;
    }
    public Book(){

    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
