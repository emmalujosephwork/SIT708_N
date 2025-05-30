package com.example.lafmap;

public class Advert {
    public int id;
    public String type;
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;

    public Advert(int id, String type, String name, String phone, String description, String date, String location) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }
}
