package com.codecool.web.dao.database;

import com.codecool.web.dao.SearchDao;
import com.codecool.web.model.search.FilteredSearch;
import com.codecool.web.model.search.Search;
import com.codecool.web.model.search.SimpleSearch;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseSearchDao extends AbstractDao implements SearchDao {

    DatabaseSearchDao(Connection connection) {
        super(connection);
    }

    @Override
    public List<SimpleSearch> getAllSimpleSearch() {
        return null;
    }

    @Override
    public List<SimpleSearch> getAllSimppleSearchBetween(LocalDateTime begins, LocalDateTime ends) {
        return null;
    }

    @Override
    public List<SimpleSearch> getSimpleSearchForUser(String userName) {
        return null;
    }

    @Override
    public List<FilteredSearch> gezSavedSearchByUser(String userName) {
        return null;
    }

    @Override
    public FilteredSearch getLastNotSavedSearchByUser(String userName) {
        return null;
    }

    @Override
    public void addNewSearch(Search search) {

    }

    @Override
    public void updateSearch(Search search) {

    }

    @Override
    public void removeSearch(int searchId) {

    }
}
