package com.codecool.web.service;

import com.codecool.web.dao.ReservationDao;
import com.codecool.web.model.Reservation;
import com.codecool.web.service.exception.NoSuchReservationException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {

    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao){
        this.reservationDao = reservationDao;
    }

    public Reservation findReservationById(int reservationId) throws SQLException, NoSuchReservationException{
        return reservationDao.findReservationById(reservationId);
    }

    public List<Reservation> getAllByRealEstate(int realEstateId) throws SQLException{
        return reservationDao.getAllByRealEstate(realEstateId);
    }

    public List<Reservation> getAllByOwner(String userName) throws SQLException{
        return reservationDao.getAllByOwner(userName);
    }

    public List<Reservation> getAllByRenter(String userName) throws SQLException{
        return reservationDao.getAllByRenter(userName);
    }

    public Reservation addReservation(int realEstateId, String renter, LocalDateTime begins, LocalDateTime ends) throws SQLException{
        return reservationDao.addReservation(realEstateId, renter, begins, ends);
    }

    public void updateReservationTime(int reservationID, LocalDateTime begins, LocalDateTime ends) throws SQLException{
        reservationDao.updateReservationTime(reservationID, begins, ends);
    }

    public void confirmReservation(int reservationID) throws SQLException{
        reservationDao.confirmReservation(reservationID);
    }

    public void removeReservation(int reservationId) throws SQLException{
        reservationDao.removeReservation(reservationId);
    }
}
