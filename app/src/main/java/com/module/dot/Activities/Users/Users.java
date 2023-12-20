package com.module.dot.Activities.Users;

import android.graphics.drawable.Drawable;

public class Users {
    private String profileImagePath;
    private String userID;
    private String adminID;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String address;
    private String companyName;
    private String position;
    private String password;
    private String dateRegistered;

    public Users() {

    }

    // Constructor for showing the user's profile.
    public Users(String profileImagePath, String firstName, String lastName, String position) {
        this.profileImagePath = profileImagePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    // Constructor for the User class.
    public Users(String profileImagePath, String firstName, String lastName, String dateOfBirth,
                 String email, String phoneNumber, String address, String companyName,
                 String position, String password_hash) {
        this.profileImagePath = profileImagePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.companyName = companyName;
        this.position = position;
        this.password = password_hash;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }


    public String getProfileImagePath() {
        return profileImagePath;
    }

    public String getUserID() {
        return userID;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
