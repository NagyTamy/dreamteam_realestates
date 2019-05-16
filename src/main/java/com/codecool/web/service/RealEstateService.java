package com.codecool.web.service;

import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.model.RealEstate;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import java.sql.SQLException;
import java.util.List;

public class RealEstateService {

    private RealEstateDao realEstateDao;

    public RealEstateService(RealEstateDao realEstateDao){
        this.realEstateDao = realEstateDao;
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

}
