package com.codecool.web.model;

import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class Reservation extends AbstractModel {

    private LocalDateTime requestDate = now();
    private int realEstateId;
    private AbstractUser renter;
    private LocalDateTime begins;
    private LocalDateTime ends;

    private boolean isConfirmed;
    private LocalDateTime confirmationDate;

    Reservation(int id, int realEstateId, AbstractUser renter, LocalDateTime begins, LocalDateTime ends) {
        super(id);
        this.realEstateId = realEstateId;
        this.renter = renter;
        this.begins = begins;
        this.ends = ends;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public int getRealEstateId() {
        return realEstateId;
    }

    public AbstractUser getRenter() {
        return renter;
    }

    public LocalDateTime getBegins() {
        return begins;
    }

    public LocalDateTime getEnds() {
        return ends;
    }

    public void setConfirmed(boolean confirmed) {
        this.isConfirmed = confirmed;
    }

    public boolean getConfirmed(){
        return isConfirmed;
    }

    public LocalDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(LocalDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
}
