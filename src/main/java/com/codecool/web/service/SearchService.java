package com.codecool.web.service;

import com.codecool.web.dao.SearchDao;
import com.codecool.web.model.search.FilteredSearch;
import com.codecool.web.model.search.SimpleSearch;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SearchService {

    private SearchDao searchDao;

    public SearchService(SearchDao searchDao){
        this.searchDao = searchDao;
    }

    public List<SimpleSearch> getAllSimpleSearch() throws SQLException{
        return searchDao.getAllSimpleSearch();
    }

    public List<SimpleSearch> getAllSimpleSearchBetween(LocalDateTime begins, LocalDateTime ends) throws SQLException{
        return searchDao.getAllSimpleSearchBetween(begins, ends);
    }

    public List<FilteredSearch> getSavedSearchByUser(String userName) throws SQLException{
        return searchDao.getSavedSearchByUser(userName);
    }

    public FilteredSearch getLastNotSavedSearchByUser(String userName) throws SQLException{
        return searchDao.getLastNotSavedSearchByUser(userName);
    }

    public void addNewSimpleSearch(String freeWordSearchKey) throws SQLException{
        searchDao.addNewSimpleSearch(freeWordSearchKey);
    }

    public void addNewFilteredSearch(String realEstateName, String country, String city, int bedCount, int priceMax, int priceMin, String extras) throws SQLException{
        searchDao.addNewFilteredSearch(realEstateName, country, city, bedCount, priceMax, priceMin, extras);
    }

    public void updateSavedSearch(FilteredSearch search) throws SQLException{
        searchDao.updateSavedSearch(search);
    }

    public void removeSearch(int searchId) throws SQLException{
        searchDao.removeSearch(searchId);
    }

}
