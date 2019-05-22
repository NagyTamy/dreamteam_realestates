package com.codecool.web.model;

import java.time.LocalDateTime;

public class RealEstate extends AbstractModel {

    private String name;
    private String country;
    private String city;
    private String address;
    private int bedCount;
    private int price;

    private String description;
    private String extras;
    private boolean isPublic = false;
    private LocalDateTime uploadDate;
    private float avgRating;

    public RealEstate(int id, String name, String country, String city, String address, int bedCount, int price) {
        super(id);
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.bedCount = bedCount;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getBedCount() {
        return bedCount;
    }

    public int getPrice() {
        return price;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPublic(){
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    

    public void setUpldoadDate(LocalDateTime upldoadDate) {
        this.uploadDate = upldoadDate;
    }

    public LocalDateTime getUpldoadDate() {
        return uploadDate;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public float getAvgRating() {
        return avgRating;
    }
}
