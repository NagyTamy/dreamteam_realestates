package com.codecool.web.model;

import java.util.List;

public class RealEstate extends AbstractModel {

    private String name;
    private String country;
    private String city;
    private String address;
    private int bedCount;
    private int price;

    private String description;
    private List<String> extras;
    private boolean isPublic;
    private List<Reservation> reservations;
    private List<Comment> reviews;
    private List<byte[]> pictures;
    private byte[] mainPicture;

    RealEstate(int id, String name, String country, String city, String address, int bedCount, int price) {
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

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(String extra) {
        extras.add(extra);
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }

    public byte[] getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(byte[] mainPicture) {
        this.mainPicture = mainPicture;
    }

    public List<byte[]> getPictures() {
        return pictures;
    }

    public void setPictures(byte[] picture) {
        pictures.add(picture);
    }

    public void setPictures(List<byte[]> pictures) {
        this.pictures = pictures;
    }

    public List<Comment> getReviews() {
        return reviews;
    }

    public void setReviewa(Comment review) {
        reviews.add(review);
    }

    public void setReviews(List<Comment> reviews) {
        this.reviews = reviews;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Reservation reservation) {
        reservations.add(reservation);
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
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
}
