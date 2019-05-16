package com.codecool.web.model.search;


import com.codecool.web.model.AbstractModel;

import java.time.LocalDateTime;

public class SimpleSearch extends Search {

    private String[] searchwords;
    private String freeWordSearchKey;

    public SimpleSearch(int id, LocalDateTime date, String freeWordSearchKey){
        super(id, date);
        this.freeWordSearchKey = freeWordSearchKey;
        freeWordSearchKey = freeWordSearchKey.replaceAll(",\\s", ",");
        searchwords = freeWordSearchKey.split(",");
    }

    public String[] getSearchwords() {
        return searchwords;
    }

    public String getFreeWordSearchKey() {
        return freeWordSearchKey;
    }
}
