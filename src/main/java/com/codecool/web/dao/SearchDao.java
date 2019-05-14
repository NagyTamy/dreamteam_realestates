package com.codecool.web.dao;

import com.codecool.web.model.search.FilteredSearch;
import com.codecool.web.model.search.Search;
import com.codecool.web.model.search.SimpleSearch;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchDao {

    List<SimpleSearch> getAllSimpleSearch();

    List<SimpleSearch> getAllSimppleSearchBetween(LocalDateTime begins, LocalDateTime ends);

    List<SimpleSearch> getSimpleSearchForUser(String userName);

    List<FilteredSearch> gezSavedSearchByUser(String userName);

    FilteredSearch getLastNotSavedSearchByUser(String userName);

    void addNewSearch(Search search);

    void updateSearch(Search search);

    void removeSearch(int searchId);
}
