package com.codecool.web.service;

import com.codecool.web.dao.PictureDao;
import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.model.Picture;
import com.codecool.web.model.RealEstate;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import java.sql.SQLException;
import java.util.List;

public class RealEstateService {

    private RealEstateDao realEstateDao;
    private PictureDao pictureDao;

    public RealEstateService(RealEstateDao realEstateDao, PictureDao pictureDao){
        this.realEstateDao = realEstateDao;
        this.pictureDao = pictureDao;
    }

    public RealEstate findRealEstateById(int realEstateId) throws SQLException, NoSuchRealEstateException{
        return realEstateDao.findRealEstateById(realEstateId);
    }

    public List<RealEstate> findRealEstatesByUser(String user) throws SQLException {
        return realEstateDao.findRealEstatesByUser(user);
    }

    public List<RealEstate> getAllRealEstate() throws SQLException{
        return realEstateDao.getAllRealEstate();
    }

    public RealEstate findByReservationId(int reservationId) throws SQLException, NoSuchRealEstateException{
        return realEstateDao.findByReservationId(reservationId);
    }

    public void addRealEstate(String name, String country, String city, String address, int bedCount, int price, String description, String extras) throws SQLException{
        realEstateDao.addRealEstate(name, country, city, address, bedCount, price, description, extras);
    }

    public void updateRealEstate(int bedCount, int price, String description, String extras, int realEstateId) throws SQLException{
        realEstateDao.updateRealEstate(bedCount, price, description, extras, realEstateId);
    }

    public void removeRealEstate(int realEstateId) throws SQLException{
        realEstateDao.removeRealEstate(realEstateId);
    }

    public void changeRealEstateState(int realEstateId) throws SQLException, NoSuchRealEstateException{
        realEstateDao.changeRealEstateState(realEstateId);
    }

    public List<RealEstate> getBestRated() throws SQLException, NoSuchPictureException{
        List<RealEstate> getBestRated = realEstateDao.getBestRated();
        return addMainPictures(getBestRated);
    }

    public List<RealEstate> getNewest() throws SQLException, NoSuchPictureException{
        List<RealEstate> getNewest = realEstateDao.getNewest();
        return addMainPictures(getNewest);
    }

    public List<RealEstate> getLastReserved() throws SQLException, NoSuchRealEstateException, NoSuchPictureException{
        List<RealEstate> getLastReserved = realEstateDao.getLastReserved();
        return addMainPictures(getLastReserved);
    }

    private List<RealEstate> addMainPictures(List<RealEstate> basicList) throws SQLException, NoSuchPictureException {
        for (RealEstate item : basicList){
            Picture picture = pictureDao.findMainForRealEstate(item.getId());
            if(picture != null){
                item.setPic(picture.getImage());
            }
        } return basicList;
    }

}
