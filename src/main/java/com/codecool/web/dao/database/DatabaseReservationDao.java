package com.codecool.web.dao.database;

import com.codecool.web.dao.ReservationDao;
import com.codecool.web.model.Reservation;

import java.sql.Connection;
import java.util.List;

public class DatabaseReservationDao extends AbstractDao implements ReservationDao {

    DatabaseReservationDao(Connection connection) {
        super(connection);
    }

    @Override
    public Reservation findReservationById(int reservationId) {
        return null;
    }

    @Override
    public List<Reservation> getAllByRealEstate(int realEstateId) {
        return null;
    }

    @Override
    public List<Reservation> getAllByOwner(String userName) {
        return null;
    }

    @Override
    public List<Reservation> getAllByRenter(String userName) {
        return null;
    }

    @Override
    public void addReservation(Reservation newReservation) {

    }

    @Override
    public void updateReservation(Reservation updateReservation) {

    }

    @Override
    public void removeReservation(int reservationId) {

    }
}
