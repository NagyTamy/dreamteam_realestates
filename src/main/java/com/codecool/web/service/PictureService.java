package com.codecool.web.service;

import com.codecool.web.dao.PictureDao;
import com.codecool.web.model.Picture;
import com.codecool.web.service.exception.NoSuchPictureException;

import java.sql.SQLException;
import java.util.List;

public class PictureService {

    private PictureDao pictureDao;

    public PictureService(PictureDao pictureDao){
        this.pictureDao = pictureDao;
    }

    public Picture findPictureById(int id) throws SQLException, NoSuchPictureException{
        return pictureDao.findPictureById(id);
    }

    public List<Picture> getAllPictureForUser(String userName) throws SQLException{
        return pictureDao.getAllPictureForUser(userName);
    }

    public List<Picture> getAllPictureForRealEstate(int realEstateId) throws SQLException{
        return pictureDao.getAllPictureForRealEstate(realEstateId);
    }

}
