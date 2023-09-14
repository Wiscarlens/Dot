package com.module.dot;

import android.graphics.drawable.Drawable;

public class Users {
    private final Drawable profileImage;
    private String userID;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private String dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String streetName;
    private String city;
    private String state;
    private int zipCode;
    private final String position;
    private String password;
    private String dateRegistered;

    // Constructor for showing the user's profile.
    public Users(Drawable profileImage, String firstName, String middleName, String lastName, String position) {
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.position = position;
    }

    // Constructor for the User class.
    public Users(Drawable profileImage, String firstName, String middleName, String lastName,
                 String dateOfBirth, String gender, String email, String phoneNumber,
                 String streetName, String city, String state, int zipCode, String position,
                 String password_hash) {
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.position = position;
        this.password = password_hash;
    }

    public String getFullName() {
        String fullName;

        if(!middleName.isEmpty()){
            fullName = firstName + " " + middleName.charAt(0) + ". " + lastName;
        } else {
            fullName = firstName + " " + lastName;
        }
        return fullName;
    }


    public String getAddress() {
        return streetName + ", " + city + "\n" + state + " " + zipCode;
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

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZipCode() {
        return zipCode;
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
