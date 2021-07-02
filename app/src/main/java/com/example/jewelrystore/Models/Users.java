package com.example.jewelrystore.Models;

public class Users {
    private String First_Name,Last_Name,Email,Phone,Password,image;
    public Users()
    {

    }

    public Users(String first_Name, String last_Name, String email, String phone, String password, String image) {
        First_Name = first_Name;
        Last_Name = last_Name;
        Email = email;
        Phone = phone;
        Password = password;
        this.image = image;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
