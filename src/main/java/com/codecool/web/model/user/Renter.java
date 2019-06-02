package com.codecool.web.model.user;

public class Renter extends AbstractUser {

    private String role = "Renter";

    public Renter(String name, String eMail) {
        super(name, eMail);
    }

    public String getRole() {
        return role;
    }
}
