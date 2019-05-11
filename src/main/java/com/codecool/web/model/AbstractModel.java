package com.codecool.web.model;

public abstract class AbstractModel {

    private int id;

    AbstractModel(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
