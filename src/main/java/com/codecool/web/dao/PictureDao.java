package com.codecool.web.dao;

import com.codecool.web.model.Picture;
import com.codecool.web.service.exception.NoSuchPictureException;

import java.sql.SQLException;
import java.util.List;

public interface PictureDao {

    Picture findPictureById(int id) throws SQLException, NoSuchPictureException;

    List<Picture> getAllPictureForUser(String userName) throws SQLException;

    List<Picture> getAllPictureForRealEstate(int realEstateId) throws SQLException;
}
