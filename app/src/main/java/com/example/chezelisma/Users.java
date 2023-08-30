package com.example.chezelisma;

import android.graphics.drawable.Drawable;

public class Users {
    private Drawable profileImage;
    private String userID;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String streetName;
    private String city;
    private String state;
    private String zipCode;
    private String position;
    private String password_hash;
    private String dateRegistered;

    public Users(Drawable profileImage, String firstName, String middleName, String lastName, String position) {
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.position = position;
    }

    public String getFullName() {
        String fullName = "";
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

    public String getZipCode() {
        return zipCode;
    }

    public String getPosition() {
        return position;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }
}
