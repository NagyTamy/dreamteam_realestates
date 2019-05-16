package com.codecool.web.dao.database;

import com.codecool.web.dao.SearchDao;
import com.codecool.web.model.search.FilteredSearch;
import com.codecool.web.model.search.Search;
import com.codecool.web.model.search.SimpleSearch;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSearchDao extends AbstractDao implements SearchDao {

    DatabaseSearchDao(Connection connection) {
        super(connection);
    }

    @Override
    public List<SimpleSearch> getAllSimpleSearch() throws SQLException{
        List<SimpleSearch> getAllSimpleSearch = new ArrayList<>();
        String sql = "SELECT * FROM searches WHERE free_search_keys IS NOT NULL";
        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                getAllSimpleSearch.add((SimpleSearch) fetchSearch(resultSet));
            }
        } return getAllSimpleSearch;
    }

    @Override
    public List<SimpleSearch> getAllSimpleSearchBetween(LocalDateTime begins, LocalDateTime ends) throws SQLException {
        List<SimpleSearch> getAllSimpleSearchBetween = new ArrayList<>();
        String sql = "SELECT * FROM searches WHERE free_search_keys IS NOT NULL AND date BETWEEN ? AND ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(begins));
            statement.setTimestamp(2,  Timestamp.valueOf(ends));
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    getAllSimpleSearchBetween.add((SimpleSearch) fetchSearch(resultSet));
                }
            }
        } return getAllSimpleSearchBetween;
    }


    @Override
    public List<FilteredSearch> getSavedSearchByUser(String userName) throws SQLException {
        List<FilteredSearch> getSavedSearchByUser = new ArrayList<>();
        String sql = "SELECT * FROM searches WHERE user_name=? AND is_saved='true'";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    getSavedSearchByUser.add((FilteredSearch) fetchSearch(resultSet));
                }
            }
        } return getSavedSearchByUser;
    }

    @Override
    public FilteredSearch getLastNotSavedSearchByUser(String userName) throws SQLException {
        String sql = "SELECT * FROM searches WHERE user_name=? AND is_saved='false' AND date = (SELECT MAX(date) FROM searches WHERE user_name=?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            statement.setString(2, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return (FilteredSearch) fetchSearch(resultSet);
                }
            }
        } return null;
    }

    @Override
    public void addNewSimpleSearch(String freeWordSearchKey) throws SQLException{
        String sql = "INSERT INTO searches(free_search_keys) VALUES (?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, freeWordSearchKey);
            executeInsert(statement);
        }
    }

    @Override
    public void addNewFilteredSearch(String realEstateName, String country, String city, int bedCount, int priceMax, int priceMin, String extras) throws SQLException {
        String sql = "INSERT INTO searches(real_estate_name, country, city, bed_count, price_min, price_max, extras) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, realEstateName);
            statement.setString(2, country);
            statement.setString(3, city);
            statement.setInt(4, bedCount);
            statement.setInt(5, priceMin);
            statement.setInt(6, priceMax);
            statement.setString(7, extras);
            executeInsert(statement);
        }
    }

    @Override
    public void updateSavedSearch(FilteredSearch search) throws SQLException {
        String sql = "UPDATE searches SET real_estate_name=?, country=?, city=?, bed_count=?, price_min=?, price_max=?, extras=? WHERE search_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, search.getRealEstateName());
            statement.setString(2, search.getCountry());
            statement.setString(3, search.getCity());
            statement.setInt(4, search.getBedCount());
            statement.setInt(5, search.getPriceMin());
            statement.setInt(6, search.getPriceMax());
            statement.setString(7, search.getExtras());
            statement.setInt(8, search.getId());
            executeInsert(statement);
        }
    }

    @Override
    public void removeSearch(int searchId) throws SQLException{
        String sql = "DELETE FROM searches WHERE search_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, searchId);
            executeInsert(statement);
        }
    }

    private Search fetchSearch(ResultSet resultSet) throws SQLException {
        Search newSearch;
        int id = resultSet.getInt("search_id");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        if (resultSet.getString("free_search_keys").isEmpty()) {
            String realEstateName = resultSet.getString("real_estate_name");
            String country = resultSet.getString("country");
            String city = resultSet.getString("city");
            int bedCount = resultSet.getInt("bed_count");
            int priceMax = resultSet.getInt("price_max");
            int priceMin = resultSet.getInt("price_min");
            String extras = resultSet.getString("extras");
            newSearch = new FilteredSearch(id, date, realEstateName, country, city, bedCount, priceMax, priceMin, extras);
            if (resultSet.getBoolean("is_saved")) {
                ((FilteredSearch) newSearch).setIsSaved(true);
            }
        } else {
            String freeWordSearchKey = resultSet.getString("free_search_keys");
            newSearch = new SimpleSearch(id, date, freeWordSearchKey);
        }
        return newSearch;
    }


        /*    search_id SERIAL PRIMARY KEY,
    date TIMESTAMP WITH TIME ZONE DEFAULT now(),
    user_name varchar(40),
    free_search_keys text,
    real_estate_name varchar(40) UNIQUE,
    country varchar(74),
    city varchar(85),
    bed_count int,
    price_min int,
    price_max int,
    extras text,
    is_saved boolean DEFAULT false,*/

}

