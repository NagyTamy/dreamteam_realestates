package com.codecool.web.dao;

import com.codecool.web.model.RealEstate;

import java.time.LocalDateTime;
import java.util.List;

public interface RealEstateDao {

    RealEstate findRealEstateById(int realEstateId);

    List<RealEstate> findRealEstatesByUser(String user);

    List<RealEstate> getAllRealEstate();

    List<RealEstate> simpleSearchResul(String searchKey);

    List<RealEstate> filteredSearchResult(String name, String country, String city, String address, int bedCount, int price, String extras, LocalDateTime begins, LocalDateTime ends);

    RealEstate findByReservationId(int reservationId);

    void addRealEstate(RealEstate realEstate);

    void updateRealEstate(RealEstate realEstate);

    void removeRealEstate(int realestateId);

}
