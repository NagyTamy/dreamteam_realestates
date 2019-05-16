package com.codecool.web.dao;

import com.codecool.web.model.search.FilteredSearch;
import com.codecool.web.model.search.SimpleSearch;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface SearchDao {

    List<SimpleSearch> getAllSimpleSearch() throws SQLException;

    List<SimpleSearch> getAllSimpleSearchBetween(LocalDateTime begins, LocalDateTime ends) throws SQLException;


    List<FilteredSearch> getSavedSearchByUser(String userName) throws SQLException;

    FilteredSearch getLastNotSavedSearchByUser(String userName) throws SQLException;

    void addNewSimpleSearch(String freeWordSearchKey) throws SQLException;

    void addNewFilteredSearch(String realEstateName, String country, String city, int bedCount, int priceMax, int priceMin, String extras) throws SQLException;

    void updateSavedSearch(FilteredSearch search) throws SQLException;

    void removeSearch(int searchId) throws SQLException;
}
