package com.example.jewelrystore.Models;

public class TestModel
{
    private String pid,Quantity;

    public TestModel()
    {

    }

    public TestModel(String pid, String quantity) {
        this.pid = pid;
        Quantity = quantity;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
