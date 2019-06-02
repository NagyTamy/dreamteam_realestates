package com.codecool.web.dao;

import com.codecool.web.model.RealEstate;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import java.sql.SQLException;
import java.util.List;

public interface RealEstateDao {

    RealEstate findRealEstateById(int realEstateId) throws SQLException, NoSuchRealEstateException;

    List<RealEstate> findRealEstatesByUser(String user) throws SQLException;

    List<RealEstate> getAllRealEstate() throws SQLException;

    RealEstate findByReservationId(int reservationId) throws SQLException, NoSuchRealEstateException;

    void addRealEstate(String currentUser, String name, String country, String city, String address, int bedCount, int price, String description, String extras) throws SQLException;

    void updateRealEstate(String currentUser, int bedCount, int price, String description, String extras, int realEstateId) throws SQLException;

    void removeRealEstate(String currentUser, int realEstateId) throws SQLException;

    void changeRealEstateState(String currentUser, int realEstateId) throws SQLException, NoSuchRealEstateException;

    List<RealEstate> getBestRated() throws SQLException;

    List<RealEstate> getNewest() throws SQLException;

    List<RealEstate> getLastReserved() throws SQLException, NoSuchRealEstateException;

    int getNumberOfRealEstates() throws SQLException;
}
