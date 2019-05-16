package com.codecool.web.model;

public class Picture {

    private int id;
    private byte[] image;
    private String description;

    public Picture(int id, byte[] image, String description){
        this.id = id;
        this.image = image;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
