package com.codecool.web.dao.database;

import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.model.RealEstate;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRealEstatetDao extends AbstractDao implements RealEstateDao {

    public DatabaseRealEstatetDao(Connection connection) {
        super(connection);
    }

    @Override
    public RealEstate findRealEstateById(int realEstateId) throws SQLException, NoSuchRealEstateException {
        String sql = "SELECT * FROM real_estates WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchRealEstate(resultSet);
                }
            }
        } throw new NoSuchRealEstateException();
    }

    @Override
    public List<RealEstate> findRealEstatesByUser(String user) throws SQLException{
        List<RealEstate> findRealEstatesByUser = new ArrayList<>();
        String sql = "SELECT * FROM real_estates WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, user);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    findRealEstatesByUser.add(fetchRealEstate(resultSet));
                }
            }
        } return findRealEstatesByUser;
    }

    @Override
    public List<RealEstate> getAllRealEstate() throws SQLException{
        List<RealEstate> getAllRealEstate = new ArrayList<>();
        String sql = "SELECT * FROM real_estates";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
                while (resultSet.next()){
                    getAllRealEstate.add(fetchRealEstate(resultSet));
            }
        } return getAllRealEstate;
    }

    @Override
    public RealEstate findByReservationId(int reservationId) throws SQLException, NoSuchRealEstateException{
        String sql = "SELECT * FROM real_estates RIGHT JOIN reservations ON real_estates.real_estate_id = reservations.real_estate_id WHERE reservation_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, reservationId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchRealEstate(resultSet);
                }
            }
        } throw new NoSuchRealEstateException();
    }

    @Override
    public void addRealEstate(String currentUser, String name, String country, String city, String address, int bedCount, int price, String description, String extras) throws SQLException {
        String sql = "SET session.osuser to ?; INSERT INTO real_estates(real_estate_name, country, city, address, bed_count, price, description, extras) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, currentUser);
            statement.setString(2, name);
            statement.setString(3, country);
            statement.setString(4, city);
            statement.setString(5, address);
            statement.setInt(6, bedCount);
            statement.setInt(7, price);
            statement.setString(8, description);
            statement.setString(9, extras);
            executeInsert(statement);
        }
    }

    @Override
    public void updateRealEstate(String currentUser, int bedCount, int price, String description, String extras, int realEstateId) throws SQLException {
        String sql = "SET session.osuser to ?; UPDATE real_estates SET bed_count=?, price=?, description=?, extras=? WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, currentUser);
            statement.setInt(2, bedCount);
            statement.setInt(3, price);
            statement.setString(4, description);
            statement.setString(5, extras);
            statement.setInt(6, realEstateId);
            executeInsert(statement);
        }
    }

    @Override
    public void removeRealEstate(String currentUser, int realEstateId) throws SQLException {
        String sql="SET session.osuser to ?; DELETE FROM real_estates WHERE id =?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, currentUser);
            statement.setInt(2, realEstateId);
            executeInsert(statement);
        }
    }

    @Override
    public void changeRealEstateState(String currentUser, int realEstateId) throws SQLException, NoSuchRealEstateException{
        boolean isPublic;
        RealEstate realEstate = findRealEstateById(realEstateId);
        if(realEstate.getPublic()){
            isPublic = false;
        } else {
            isPublic = true;
        }

        String sql = "SET session.osuser to ?; UPDATE real_estates SET is_public=? WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, currentUser);
            statement.setBoolean(2, isPublic);
            statement.setInt(3, realEstateId);
            executeInsert(statement);
        }
    }

    @Override
    public List<RealEstate> getBestRated() throws SQLException {
        List<RealEstate> getBestRated = new ArrayList<>();
        String sql = "SELECT * FROM real_estates ORDER BY avg_rating LIMIT 4";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                getBestRated.add(fetchRealEstate(resultSet));
            }
        } return getBestRated;
    }

    @Override
    public List<RealEstate> getNewest() throws SQLException {
        List<RealEstate> getNewest = new ArrayList<>();
        String sql = "SELECT * FROM real_estates ORDER BY upload_date DESC LIMIT 4";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                getNewest.add(fetchRealEstate(resultSet));
            }
        } return getNewest;
    }

    @Override
    public List<RealEstate> getLastReserved() throws SQLException, NoSuchRealEstateException {
        List<RealEstate> getNewest = new ArrayList<>();
        String sql = "SELECT DISTINCT real_estates.real_estate_id, reservation_conformation_date FROM real_estates LEFT JOIN reservations ON reservations.real_estate_id = real_estates.real_estate_id ORDER BY reservation_conformation_date DESC LIMIT 4";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                getNewest.add(findRealEstateById(resultSet.getInt("real_estate_id")));
            }
        } return getNewest;
    }

    private RealEstate fetchRealEstate(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("real_estate_id");
        String name = resultSet.getString("real_estate_name");
        String country = resultSet.getString("country");
        String city = resultSet.getString("city");
        String address = resultSet.getString("address");
        int bedCount = resultSet.getInt("bed_count");
        int price = resultSet.getInt("price");

        RealEstate realEstate = new RealEstate(id, name, country, city, address, bedCount, price);

        if(!(resultSet.getString("description").isEmpty())){
            realEstate.setDescription(resultSet.getString("description"));
        }
        if(!(resultSet.getString("extras").isEmpty())){
            realEstate.setExtras(resultSet.getString("extras"));
        }
        if(resultSet.getBoolean("is_public")){
            realEstate.setPublic(true);
        }
        realEstate.setAvgRating(resultSet.getFloat("avg_rating"));

        realEstate.setUploadDate(resultSet.getTimestamp("upload_date").toLocalDateTime());
        return realEstate;
    }

}
