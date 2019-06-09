package com.codecool.web.dto;

import com.codecool.web.model.RealEstate;

import java.util.List;

public class SearchResultDto {

    private List<RealEstate> searchResult;
    private boolean hasResult;

    public SearchResultDto(List<RealEstate> searchResult){
        this.searchResult = searchResult;
        this.hasResult = hasResult();
    }

    public List<RealEstate> getSearchResult() {
        return searchResult;
    }

    private boolean hasResult(){
        return searchResult.size() != 0;
    }

    public boolean isHasResult() {
        return hasResult;
    }
}
