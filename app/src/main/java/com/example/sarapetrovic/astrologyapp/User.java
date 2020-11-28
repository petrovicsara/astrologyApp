package com.example.sarapetrovic.astrologyapp;

import java.net.URI;

/**
 * Created by sarapetrovic on 7/6/19.
 */

public class User {
    private String name;
    private String email;
    private String password;
    private String dateOB;
    private String description;
    private String genderPreference;
    private String gender;
    private URI photoUrl;
    private String sign;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, String dateOB, String description, String genderPreference, String gender, String sign) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateOB = dateOB;
        this.description = description;
        this.genderPreference = genderPreference;
        this.gender = gender;
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOB() {
        return dateOB;
    }

    public void setDateOB(String dateOB) {
        this.dateOB = dateOB;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public URI getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(URI photoUrl) {
        this.photoUrl = photoUrl;
    }
}
