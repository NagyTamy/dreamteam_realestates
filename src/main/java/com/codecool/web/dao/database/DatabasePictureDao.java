package com.codecool.web.dao.database;

import com.codecool.web.dao.PictureDao;
import com.codecool.web.model.Picture;
import com.codecool.web.service.exception.NoSuchPictureException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabasePictureDao extends AbstractDao implements PictureDao {

    DatabasePictureDao(Connection connection) {
        super(connection);
    }

    @Override
    public Picture findPictureById(int id) throws SQLException, NoSuchPictureException {
        String sql ="SELECT * FROM pictures WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchPicture(resultSet);
                }
            }
        } throw new NoSuchPictureException();
    }

    @Override
    public List<Picture> getAllPictureForUser(String userName) throws SQLException {
        List<Picture> getAllPictureForUser = new ArrayList<>();
        String sql = "SELECT * FROM pictures WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    getAllPictureForUser.add(fetchPicture(resultSet));
                }
            }
        } return getAllPictureForUser;
    }

    @Override
    public List<Picture> getAllPictureForRealEstate(int realEstateId) throws SQLException {
        List<Picture> getAllPictureForRealEstate = new ArrayList<>();
        String sql = "SELECT * FROM pictures WHERE real_estate=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    getAllPictureForRealEstate.add(fetchPicture(resultSet));
                }
            }
        } return getAllPictureForRealEstate;
    }

    private Picture fetchPicture(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id");
        byte[] image = resultSet.getBytes("picture");
        String description = resultSet.getString("description");
        return new Picture(id, image, description);
    }

    /*pictures(
    id SERIAL PRIMARY KEY,
    user_name varchar(40),
    real_estate int,
    picture bytea NOT NULL,
    description text DEFAULT NULL,*/
}
