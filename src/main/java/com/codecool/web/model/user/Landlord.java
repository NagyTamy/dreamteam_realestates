package com.codecool.web.model.user;

import com.codecool.web.model.RealEstate;
import com.codecool.web.model.Reservation;

import java.util.List;

public class Landlord extends AbstractUser {

    private List<RealEstate> ownedRealEstate;
    private List<Reservation> incomingReservations;

    public Landlord(int id, String name, String eMail, String password) {
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
}
