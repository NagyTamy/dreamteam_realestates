package com.codecool.web.dao;

import com.codecool.web.model.Reservation;
import com.codecool.web.service.exception.NoSuchReservationException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationDao {

    Reservation findReservationById(int reservationId) throws SQLException, NoSuchReservationException;

    List<Reservation> getAllByRealEstate(int realEstateId) throws SQLException;

    List<Reservation> getAllByOwner(String userName) throws SQLException;

    List<Reservation> getAllByRenter(String userName) throws SQLException;

    Reservation addReservation(int realEstateId, String renter, LocalDateTime begins, LocalDateTime ends) throws SQLException;

    void updateReservationTime(int reservationID, LocalDateTime begins, LocalDateTime ends) throws SQLException;

    void confirmReservation(int reservationID) throws SQLException;

    void removeReservation(int reservationId) throws SQLException;
}
