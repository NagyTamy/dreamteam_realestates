package com.codecool.web.dao.database;

import com.codecool.web.dao.RealEstateDao;
import com.codecool.web.model.RealEstate;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseRealEstatetDao extends AbstractDao implements RealEstateDao {

    DatabaseRealEstatetDao(Connection connection) {
        super(connection);
    }

    @Override
    public RealEstate findRealEstateById(int realEstateId) {
        return null;
    }

    @Override
    public List<RealEstate> findRealEstatesByUser(String user) {
        return null;
    }

    @Override
    public List<RealEstate> getAllRealEstate() {
        return null;
    }

    @Override
    public List<RealEstate> simpleSearchResul(String searchKey) {
        return null;
    }

    @Override
    public List<RealEstate> filteredSearchResult(String name, String country, String city, String address, int bedCount, int price, String extras, LocalDateTime begins, LocalDateTime ends) {
        return null;
    }

    @Override
    public RealEstate findByReservationId(int reservationId) {
        return null;
    }

    @Override
    public void addRealEstate(RealEstate realEstate) {

    }

    @Override
    public void updateRealEstate(RealEstate realEstate) {

    }

    @Override
    public void removeRealEstate(int realestateId) {

    }
}
