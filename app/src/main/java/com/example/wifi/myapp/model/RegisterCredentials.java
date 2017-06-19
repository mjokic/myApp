package com.example.wifi.myapp.model;


public class RegisterCredentials {

    private String name;
    private String email;
    private String password;
    private String address;
    private String picture;
    private String phone;

    public RegisterCredentials(String name, String email, String password,
                               String address, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.picture = "default";
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}