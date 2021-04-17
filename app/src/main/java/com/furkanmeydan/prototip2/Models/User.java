package com.furkanmeydan.prototip2.Models;

public class User {

    private String
                    nameSurname,
                    birthDate,
                    profilePicture,
                    email,
                    gender,
                    oneSignalID;

    public User(){

    }

    public User(String nameSurname, String birthDate, String profilePicture, String email, String gender, String oneSignalID) {
        this.nameSurname = nameSurname;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;
        this.email = email;
        this.gender = gender;
        this.oneSignalID = oneSignalID;
    }

    public String getGender() {
        return gender;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public String getOneSignalID() {
        return oneSignalID;
    }
}

