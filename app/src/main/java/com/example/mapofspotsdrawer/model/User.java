package com.example.mapofspotsdrawer.model;

import java.time.LocalDate;

public class User {
    private String name;

    private String email;

    private String phone;

    private String birthday;

    private String registrationDate;

    public User() {
    }

    public User(String name, String email, String phone, String birthday, String registrationDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthDate) {
        this.birthday = birthday;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate=" + birthday +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
