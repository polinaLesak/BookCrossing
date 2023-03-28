package com.example.bookcrossing.models;

import android.net.Uri;

public class User {
    //public Object getUserId;
    private String name, surname, email, password, phone,  lastMassage, userId;

    private String  profilePic;
    public User(String name, String email, String password, String phone, String  profilePic, String lastMassage, String userId) {
        this.name = name;

        this.email = email;
        this.password = password;
        this.phone = phone;
        this.profilePic = profilePic;
        this.lastMassage = lastMassage;
        this.userId = userId;
    }

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLastMassage() {
        return lastMassage;
    }

    public void setLastMassage(String lastMassage) {
        this.lastMassage = lastMassage;
    }



    public void setName(String name) {
        this.name = name;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public User(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }


    public void setUserId(String key) {
        this.userId = key;

    }
}