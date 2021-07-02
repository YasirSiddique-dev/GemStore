package com.example.jewelrystore.Models;

public class Order {
    private String Order_ID,Date,Confirmation,State,Total_Amount,Phone,Email,City,Home_Address,First_Name,Last_Name,Time;

    public Order()
    {

    }

    public Order(String order_ID, String date, String confirmation, String state, String total_Amount, String phone, String email, String city, String home_Address, String first_Name, String last_Name, String time) {
        Order_ID = order_ID;
        Date = date;
        Confirmation = confirmation;
        State = state;
        Total_Amount = total_Amount;
        Phone = phone;
        Email = email;
        City = city;
        Home_Address = home_Address;
        First_Name = first_Name;
        Last_Name = last_Name;
        Time = time;
    }

    public String getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(String order_ID) {
        Order_ID = order_ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getConfirmation() {
        return Confirmation;
    }

    public void setConfirmation(String confirmation) {
        Confirmation = confirmation;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        Total_Amount = total_Amount;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getHome_Address() {
        return Home_Address;
    }

    public void setHome_Address(String home_Address) {
        Home_Address = home_Address;
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
