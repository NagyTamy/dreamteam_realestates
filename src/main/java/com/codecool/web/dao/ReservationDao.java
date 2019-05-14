package com.codecool.web.dao;

import com.codecool.web.model.Reservation;

import java.util.List;

public interface ReservationDao {

    Reservation findReservationById(int reservationId);

    List<Reservation> getAllByRealEstate(int realEstateId);

    List<Reservation> getAllByOwner(String userName);

    List<Reservation> getAllByRenter(String userName);

    void addReservation(Reservation newReservation);

    void updateReservation(Reservation updateReservation);

    void removeReservation(int reservationId);
}
