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
    public void addRealEstate(String name, String country, String city, String address, int bedCount, int price, String description, String extras) throws SQLException {
        String sql = "INSERT INTO real_estates(real_estate_name, country, city, address, bed_count, price, description, extras) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, name);
            statement.setString(2, country);
            statement.setString(3, city);
            statement.setString(4, address);
            statement.setInt(5, bedCount);
            statement.setInt(6, price);
            statement.setString(7, description);
            statement.setString(8, extras);
            executeInsert(statement);
        }
    }

    @Override
    public void updateRealEstate(int bedCount, int price, String description, String extras, int realEstateId) throws SQLException {
        String sql = "UPDATE real_estates SET bed_count=?, price=?, description=?, extras=? WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, bedCount);
            statement.setInt(2, price);
            statement.setString(3, description);
            statement.setString(4, extras);
            statement.setInt(5, realEstateId);
            executeInsert(statement);
        }
    }

    @Override
    public void removeRealEstate(int realEstateId) throws SQLException {
        String sql="DELETE FROM real_estates WHERE id =?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            executeInsert(statement);
        }
    }

    @Override
    public void changeRealEstateState(int realEstateId) throws SQLException, NoSuchRealEstateException{
        boolean isPublic;
        RealEstate realEstate = findRealEstateById(realEstateId);
        if(realEstate.getPublic()){
            isPublic = false;
        } else {
            isPublic = true;
        }

        String sql = "UPDATE real_estates SET is_public=? WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setBoolean(1, isPublic);
            statement.setInt(2, realEstateId);
            executeInsert(statement);
        }
    }

    @Override
    public List<RealEstate> getBestRated() throws SQLException {
        List<RealEstate> getBestRated = new ArrayList<>();
        String sql = "SELECT * FROM real_estates WHERE is_public='true' ORDER BY avg_rating DESC LIMIT 4";
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
        String sql = "SELECT * FROM real_estates WHERE is_public='true' ORDER BY upload_date DESC LIMIT 4";
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
        String sql = "SELECT DISTINCT real_estates.real_estate_id, reservation_conformation_date FROM real_estates LEFT JOIN reservations ON reservations.real_estate_id = real_estates.real_estate_id WHERE is_public='true' ORDER BY reservation_conformation_date DESC LIMIT 4";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                getNewest.add(findRealEstateById(resultSet.getInt("real_estate_id")));
            }
        } return getNewest;
    }

    @Override
    public int getNumberOfRealEstates() throws SQLException {
        String sql = "SELECT count(real_estate_id) as num FROM real_estates WHERE is_public='true'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            if (resultSet.next()){
                return resultSet.getInt("num");
            }
        } return 0;
    }

    @Override
    public List<RealEstate> findFavouritesForUser(String userName) throws SQLException{
        List<RealEstate> findFavouritesForUser = new ArrayList<>();
        String sql = "SELECT * FROM favourites LEFT JOIN real_estates re on favourites.real_estate_id = re.real_estate_id WHERE favourites.user_name=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    findFavouritesForUser.add(fetchRealEstate(resultSet));
                }
            }
        } return findFavouritesForUser;
    }

    @Override
    public void removeFromFavourites(String userName, int realEstateId) throws SQLException {
        String sql="DELETE FROM favourites WHERE user_name=? AND real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            statement.setInt(2, realEstateId);
            executeInsert(statement);
        }
    }

    @Override
    public void addToFavourites(String userName, int realEstateId) throws SQLException {
        String sql="INSERT INTO favourites(user_name, real_estate_id) VALUES (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            statement.setInt(2, realEstateId);
            executeInsert(statement);
        }
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
