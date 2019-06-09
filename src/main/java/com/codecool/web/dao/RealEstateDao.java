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

    void addRealEstate(String name, String country, String city, String address, int bedCount, int price, String description, String extras) throws SQLException;

    void updateRealEstate(int bedCount, int price, String description, String extras, int realEstateId) throws SQLException;

    void removeRealEstate(int realEstateId) throws SQLException;

    void changeRealEstateState(int realEstateId) throws SQLException, NoSuchRealEstateException;

    List<RealEstate> getBestRated() throws SQLException;

    List<RealEstate> getNewest() throws SQLException;

    List<RealEstate> getLastReserved() throws SQLException, NoSuchRealEstateException;

    int getNumberOfRealEstates() throws SQLException;

    List<RealEstate> findFavouritesForUser(String userName) throws SQLException;

    void removeFromFavourites(String userName, int realEstateId) throws SQLException;

    void addToFavourites(String userName, int realEstateId) throws SQLException;

    List<RealEstate> doSimpleSearch(String searchKey) throws SQLException;

    List<RealEstate> searchByRealEstateName(String realEstateName) throws SQLException;

    boolean isOwner(int realEstate, String userName) throws SQLException;

}
