package com.codecool.web.dao.database;

import com.codecool.web.dao.PictureDao;
import com.codecool.web.model.Picture;
import com.codecool.web.service.exception.NoSuchPictureException;


import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabasePictureDao extends AbstractDao implements PictureDao {

    public DatabasePictureDao(Connection connection) {
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

    @Override
    public void insertPicture(String imgName, String userName, int realEstateId, String description) throws SQLException, IOException {
        BufferedImage image = ImageIO.read(new File(imgName));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        outputStream.flush();
        byte[] encodedImage = outputStream.toByteArray();

        if (realEstateId == 0) {
            String sql = "INSERT INTO pictures(user_name, picture, description) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userName);
                statement.setBytes(2, encodedImage);
                statement.setString(3, description);
                executeInsert(statement);
            }
        } else {
            String sql = "INSERT INTO pictures(user_name, real_estate, picture, description) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userName);
                statement.setInt(2, realEstateId);
                statement.setBytes(3, encodedImage);
                statement.setString(4, description);
                executeInsert(statement);
            }
        }
    }

    @Override
    public void updatePicture(int imgId, String description) throws SQLException{
        String sql = "UPDATE pictures SET description=? WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, description);
            statement.setInt(2, imgId);
            executeInsert(statement);
        }
    }

    @Override
    public void deletePicture(int imgId) throws SQLException{
        String sql = "DELETE FROM pictures WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, imgId);
            executeInsert(statement);
        }
    }


    private Picture fetchPicture(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id");
        byte[] image = resultSet.getBytes("picture");
        String description = resultSet.getString("description");
        return new Picture(id, image, description);
    }

    @Override
    public Picture findMainForRealEstate(int realEstateId) throws SQLException, NoSuchPictureException{
        String sql = "SELECT * FROM main_pictures LEFT JOIN pictures p on main_pictures.picture_id = p.id WHERE real_estate_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return fetchPicture(resultSet);
            }
        } throw new NoSuchPictureException();
    }

    @Override
    public Picture findMainForUser(String userName) throws SQLException, NoSuchPictureException{
        String sql = "SELECT * FROM main_pictures LEFT JOIN pictures p on main_pictures.picture_id = p.id WHERE main_pictures.user_name = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return fetchPicture(resultSet);
            }
        } throw new NoSuchPictureException();
    }

}
