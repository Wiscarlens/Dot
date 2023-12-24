package com.module.dot.Activities.Users;

import androidx.annotation.NonNull;

public class Users {
    private String globalID;
    private String localID;
    private String creatorID;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String address;
    private String profileImagePath;
    private String companyName;
    private String positionTitle;
    private String password_hash;
    private String dateRegistered;

    public Users() {

    }

    // Constructor for showing the user's profile.
    public Users(String firstName, String lastName, String positionTitle, String profileImagePath) {
        this.profileImagePath = profileImagePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.positionTitle = positionTitle;
    }

    public Users(String globalID, String localID, String creatorID, String firstName,
                 String lastName, String dateOfBirth, String email, String phoneNumber,
                 String address, String profileImagePath, String companyName,
                 String positionTitle, String password_hash, String dateRegistered) {
        this.globalID = globalID;
        this.localID = localID;
        this.creatorID = creatorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profileImagePath = profileImagePath;
        this.companyName = companyName;
        this.positionTitle = positionTitle;
        this.password_hash = password_hash;
        this.dateRegistered = dateRegistered;
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
        this.password_hash = password_hash;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    public String getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID = localID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
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

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
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

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
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
                "globalID='" + globalID + '\'' +
                ", localID='" + localID + '\'' +
                ", creatorID='" + creatorID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", profileImagePath='" + profileImagePath + '\'' +
                ", companyName='" + companyName + '\'' +
                ", positionTitle='" + positionTitle + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", dateRegistered='" + dateRegistered + '\'' +
                '}';
    }
}
