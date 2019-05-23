package com.codecool.web.dao;

import com.codecool.web.model.Picture;
import com.codecool.web.service.exception.NoSuchPictureException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PictureDao {

    Picture findPictureById(int id) throws SQLException, NoSuchPictureException;

    List<Picture> getAllPictureForUser(String userName) throws SQLException;

    List<Picture> getAllPictureForRealEstate(int realEstateId) throws SQLException;

    void insertPicture(String imgName, String userName, int realEstateId, String description) throws SQLException, FileNotFoundException, IOException;

    void updatePicture(int imgId, String description) throws SQLException;

    void deletePicture(int imgId) throws SQLException;

    Picture findMainForRealEstate(int RealEstateId) throws SQLException, NoSuchPictureException;

    Picture findMainForUser(String userName) throws SQLException, NoSuchPictureException;
}

