package com.codecool.web.model.user;

import com.codecool.web.model.RealEstate;
import com.codecool.web.model.Reservation;
import com.codecool.web.model.messages.SystemMessages;

import java.util.List;

public class Admin extends AbstractUser {

    private List<RealEstate> ownedRealEstate;
    private List<Reservation> incomingReservations;
    private List<SystemMessages> systemMessages;
    private List<RealEstate> systemRealEstates;

    public Admin(int id, String name, String eMail, String password) {
        super(id, name, eMail, password);
    }

    public List<RealEstate> getOwnedRealEstate() {
        return ownedRealEstate;
    }

    public void setOwnedRealEstate(RealEstate realEstate) {
        ownedRealEstate.add(realEstate);
    }

    public void setOwnedRealEstate(List<RealEstate> ownedRealEstate) {
        this.ownedRealEstate = ownedRealEstate;
    }

    public List<Reservation> getIncomingReservations() {
        return incomingReservations;
    }

    public void setIncomingReservations(Reservation reservation) {
        incomingReservations.add(reservation);
    }

    public void setIncomingReservations(List<Reservation> incomingReservations) {
        this.incomingReservations = incomingReservations;
    }

    public List<SystemMessages> getSystemMessages() {
        return systemMessages;
    }

    public List<RealEstate> getSystemRealEstates() {
        return systemRealEstates;
    }

    public void setSystemMessages(SystemMessages message) {
        systemMessages.add(message);
    }

    public void setSystemMessages(List<SystemMessages> systemMessages) {
        this.systemMessages = systemMessages;
    }

    public void setSystemRealEstates(RealEstate realEstate) {
        systemRealEstates.add(realEstate);
    }

    public void setSystemRealEstates(List<RealEstate> systemRealEstates) {
        this.systemRealEstates = systemRealEstates;
    }
}
