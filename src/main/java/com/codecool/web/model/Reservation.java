package com.codecool.web.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation extends AbstractModel {

    private LocalDateTime requestDate;
    private int realEstateId;
    private String renter;
    private LocalDateTime begins;
    private LocalDateTime ends;
    private String stringEnds;
    private String stringBegins;

    private boolean isConfirmed;
    private LocalDateTime confirmationDate;
    private RealEstate realEstate;

    public Reservation(int id, int realEstateId, String renter, LocalDateTime begins, LocalDateTime ends) {
        super(id);
        this.realEstateId = realEstateId;
        this.renter = renter;
        this.begins = begins;
        this.ends = ends;
        stringEnds = timeStampToString(ends);
        stringBegins = timeStampToString(begins);
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

    private String timeStampToString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public String getStringEnds(){
        return stringEnds;
    }

    public String getStringBegins(){
        return stringBegins;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }
}
