package com.codecool.web.model;

import java.time.LocalDateTime;

public class Reservation extends AbstractModel {

    private LocalDateTime requestDate;
    private int realEstateId;
    private String renter;
    private LocalDateTime begins;
    private LocalDateTime ends;

    private boolean isConfirmed;
    private LocalDateTime confirmationDate;

    public Reservation(int id, int realEstateId, String renter, LocalDateTime begins, LocalDateTime ends) {
        super(id);
        this.realEstateId = realEstateId;
        this.renter = renter;
        this.begins = begins;
        this.ends = ends;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public int getRealEstateId() {
        return realEstateId;
    }

    public String getRenter() {
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
