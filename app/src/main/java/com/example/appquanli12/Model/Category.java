package com.example.appquanli12.Model;

public class Category {
    private String Name,Img;

    public Category() {
    }

    public Category(String name, String img) {
        Name = name;
        Img = img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }
}
