package com.codecool.web.dto;

import com.codecool.web.model.RealEstate;
import com.codecool.web.service.RealEstateService;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import java.sql.SQLException;
import java.util.List;

public class RealEstateOffersDto {

    private List<RealEstate> newest;
    private List<RealEstate> bestRated;
    private List<RealEstate> trending;
    private List<String> menuList;
    private RealEstate randomOffer;
    private String userName;
    private boolean isMyFav;
    private RealEstateService realEstateService;

    public RealEstateOffersDto(RealEstateService realEstateService, List<String> menuList, String userName) throws SQLException, NoSuchPictureException, NoSuchRealEstateException {
        this.newest = realEstateService.isMyFav(userName, realEstateService.getNewest());
        this.bestRated = realEstateService.isMyFav(userName, realEstateService.getBestRated());
        this.trending = realEstateService.isMyFav(userName, realEstateService.getLastReserved());
        this.menuList = menuList;
        this.randomOffer = realEstateService.isMyFav(userName, realEstateService.getRandomRealEstate());
        this.userName = userName;
    }

    public RealEstateOffersDto(RealEstateService realEstateService, List<String> menuList) throws SQLException, NoSuchPictureException, NoSuchRealEstateException {
        this.newest = realEstateService.getNewest();
        this.bestRated = realEstateService.getBestRated();
        this.trending = realEstateService.getLastReserved();
        this.menuList = menuList;
        this.randomOffer = realEstateService.getRandomRealEstate();
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

    public RealEstate getRandomOffer() {
        return randomOffer;
    }

    public String getUserName() {
        return userName;
    }
}
