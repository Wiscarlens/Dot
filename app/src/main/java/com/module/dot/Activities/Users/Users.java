package com.module.dot.Activities.Users;

import android.graphics.drawable.Drawable;

public class Users {
    private final Drawable profileImage;
    private String userID;
    private final String firstName;
    private final String lastName;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String address;
    private final String position;
    private String password;
    private String dateRegistered;

    // Constructor for showing the user's profile.
    public Users(Drawable profileImage, String firstName, String lastName, String position) {
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    // Constructor for the User class.
    public Users(Drawable profileImage, String firstName, String lastName, String dateOfBirth,
                 String email, String phoneNumber, String address,
                 String position, String password_hash) {
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.position = position;
        this.password = password_hash;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }


    public Drawable getProfileImage() {
        return profileImage;
    }

    public String getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPosition() {
        return position;
    }

    public String getPassword() {
        return password;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }
}
