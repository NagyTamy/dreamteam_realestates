package com.codecool.web.service;

import com.codecool.web.dao.PictureDao;
import com.codecool.web.model.Picture;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.exception.NoSuchPictureException;

import java.io.IOException;
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

    public void insertPicture(String imgName, String userName, int realEstateId, String description) throws SQLException, IOException {
        pictureDao.insertPicture(imgName, userName, realEstateId, description);
    }

    public void updatePicture(int imgId, String description) throws SQLException{
        pictureDao.updatePicture(imgId, description);
    }

    public void deletePicture(int imgId) throws SQLException{
        pictureDao.deletePicture(imgId);
    }

    public Picture findMainForRealEstate(int realEstateId) throws SQLException, NoSuchPictureException{
        return pictureDao.findMainForRealEstate(realEstateId);
    }

    public Picture findMainForUser(String userName) throws SQLException, NoSuchPictureException{
        return pictureDao.findMainForUser(userName);
    }

    public List<AbstractUser> setMainForUserList(List<AbstractUser> list) throws SQLException, NoSuchPictureException{
        for (AbstractUser user : list){
            user.setPic(findMainForUser(user.getName()).getImage());
        } return list;
    }

}
