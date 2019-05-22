package com.codecool.web.dto;

import com.codecool.web.model.RealEstate;

import java.util.List;

public class RealEstateOffersDto {

    private List<RealEstate> newest;
    private List<RealEstate> bestRated;
    private List<RealEstate> trending;
    private List<String> menuList;

    public RealEstateOffersDto(List<RealEstate> newest, List<RealEstate> bestRated, List<RealEstate> trending, List<String> menuList){
        this.newest = newest;
        this.bestRated = bestRated;
        this.trending = trending;
        this.menuList = menuList;
    }

    public List<RealEstate> getBestRated() {
        return bestRated;
    }

    public List<RealEstate> getNewest() {
        return newest;
    }

    public List<RealEstate> getTrending() {
        return trending;
    }

    public List<String> getMenuList() {
        return menuList;
    }
}
