package com.module.dot.Activities.Users;

import androidx.annotation.NonNull;

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
    private String positionTitle;
    private String password;
    private String dateRegistered;

    public Users() {

    }

    // Constructor for showing the user's profile.
    public Users(String profileImagePath, String firstName, String lastName, String positionTitle) {
        this.profileImagePath = profileImagePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.positionTitle = positionTitle;
    }

    // Constructor for the User class.
    public Users(String profileImagePath, String firstName, String lastName, String dateOfBirth,
                 String email, String phoneNumber, String address, String companyName,
                 String positionTitle, String password_hash) {
        this.profileImagePath = profileImagePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.companyName = companyName;
        this.positionTitle = positionTitle;
        this.password = password_hash;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @NonNull
    @Override
    public String toString() {
        return "Users{" +
                "profileImagePath='" + profileImagePath + '\'' +
                ", userID='" + userID + '\'' +
                ", adminID='" + adminID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", companyName='" + companyName + '\'' +
                ", positionTitle='" + positionTitle + '\'' +
                ", password='" + password + '\'' +
                ", dateRegistered='" + dateRegistered + '\'' +
                '}';
    }
}
