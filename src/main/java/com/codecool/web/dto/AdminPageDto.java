package com.codecool.web.dto;

import com.codecool.web.model.Log;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.service.*;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchRealEstateException;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminPageDto {

    private RealEstate randomOffer;
    private boolean hasRealEstates;
    private List<RealEstate> ownRealEstates;
    private boolean hasPendingRequest;
    private List<SystemMessages> allPendingUserRequest;
    private List<Log> allLogs;
    private List<String> asideMenu = new ArrayList<>(){{add("Requests"); add("Logs"); add("System real estates");}};

    public AdminPageDto(RealEstateService realEstateService, MessageService messageService, LogService logService)
            throws SQLException, NoSuchRealEstateException, NoSuchPictureException, NoSuchUserException {
        this.randomOffer = realEstateService.getRandomRealEstate();
        this.ownRealEstates = realEstateService.addMainPictures(realEstateService.findRealEstatesByUser("system"));
        this.hasRealEstates = setHasRealEstate();
        this.allPendingUserRequest = messageService.getAllPendingSystemRequest();
        this.hasPendingRequest = setHasPendingRequest();
        this.allLogs = logService.getLogs();
    }

    public RealEstate getRandomOffer() {
        return randomOffer;
    }

    public List<RealEstate> getOwnRealEstates() {
        return ownRealEstates;
    }

    private boolean setHasRealEstate(){
        if(ownRealEstates.size() > 0){
            hasRealEstates = true;
        } else {
            hasRealEstates = false;
        } return hasRealEstates;
    }

    public boolean isHasRealEstates() {
        return hasRealEstates;
    }

    private boolean setHasPendingRequest(){
        if(allPendingUserRequest.size() > 0){
            hasPendingRequest = true;
        } else {
            hasPendingRequest = false;
        } return hasPendingRequest;
    }

    public boolean isHasPendingRequest() {
        return hasPendingRequest;
    }

    public List<SystemMessages> getAllPendingUserRequest() {
        return allPendingUserRequest;
    }

    public List<Log> getAllLogs() {
        return allLogs;
    }

    public List<String> getAsideMenu() {
        return asideMenu;
    }
}
