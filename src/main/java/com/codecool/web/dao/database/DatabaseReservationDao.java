package com.codecool.web.dao.database;

import com.codecool.web.dao.ReservationDao;
import com.codecool.web.model.Reservation;
import com.codecool.web.service.exception.NoSuchReservationException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseReservationDao extends AbstractDao implements ReservationDao {

    DatabaseReservationDao(Connection connection) {
        super(connection);
    }

    @Override
    public Reservation findReservationById(int reservationId) throws SQLException, NoSuchReservationException {
        String sql = "SELECT * FROM reservations WHERE reservation_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, reservationId);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return fetchReservation(resultSet);
                }
            }

        } throw new NoSuchReservationException();
    }

    @Override
    public List<Reservation> getAllByRealEstate(int realEstateId) throws SQLException{
        List<Reservation> getAllByRealEstate = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    getAllByRealEstate.add(fetchReservation(resultSet));
                } return getAllByRealEstate;
            }
        }
    }

    @Override
    public List<Reservation> getAllByOwner(String userName) throws SQLException {
        List<Reservation> getAllByOwner = new ArrayList<>();
        String sql = "SELECT * FROM reservations LEFT JOIN real_estates re on reservations.real_estate_id = re.real_estate_id WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    getAllByOwner.add(fetchReservation(resultSet));
                } return getAllByOwner;
            }
        }
    }

    @Override
    public List<Reservation> getAllByRenter(String userName) throws SQLException{
        List<Reservation> getAllByRenter = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE tenant_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    getAllByRenter.add(fetchReservation(resultSet));
                } return getAllByRenter;
            }
        }
    }

    @Override
    public Reservation addReservation(int realEstateId, String renter, LocalDateTime begins, LocalDateTime ends) throws SQLException {
        String sql = "INSERT INTO reservations (real_estate_id, tenant_name, begins, ends) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, realEstateId);
            statement.setString(2, renter);
            statement.setTimestamp(3, Timestamp.valueOf(begins));
            statement.setTimestamp(4, Timestamp.valueOf(ends));
            executeInsert(statement);
            int id = fetchGeneratedId(statement);
            return new Reservation(id, realEstateId, renter, begins, ends);
        }
    }

    @Override
    public void updateReservationTime(int reservationID, LocalDateTime begins, LocalDateTime ends) throws SQLException {
        String sql = "UPDATE reservations SET begins=?, ends=?, is_confirmed='false' WHERE reservation_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(begins));
            statement.setTimestamp(2, Timestamp.valueOf(ends));
            statement.setInt(3, reservationID);
            executeInsert(statement);

        }
    }

    @Override
    public void confirmReservation(int reservationID) throws SQLException{
        String sql = "UPDATE reservations SET is_confirmed='true' WHERE reservation_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, reservationID);
            executeInsert(statement);

        }
    }

    @Override
    public void removeReservation(int reservationId) throws SQLException{
        String sql = "DELETE FROM reservations WHERE reservation_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, reservationId);
            executeInsert(statement);

        }
    }

    protected Reservation fetchReservation(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("reservation_id");
        int realEstateId = resultSet.getInt("real_estate_id");
        String renter = resultSet.getString("tenant_name");
        LocalDateTime begins = resultSet.getTimestamp("begins").toLocalDateTime();
        LocalDateTime ends = resultSet.getTimestamp("ends").toLocalDateTime();

        Reservation reservation = new Reservation(id, realEstateId, renter, begins, ends);
        reservation.setRequestDate(resultSet.getTimestamp("reservation_request_date").toLocalDateTime());
        reservation.setConfirmed(resultSet.getBoolean("is_confirmed"));
        if(reservation.getConfirmed()){
            reservation.setConfirmationDate(resultSet.getTimestamp("reservation_conformation_date").toLocalDateTime());
        } return reservation;
    }

}
