package com.codecool.web.dto;

import com.codecool.web.model.Log;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.service.*;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchRealEstateException;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.SQLException;
import java.util.List;

public class AdminPageDto {

    private RealEstate randomOffers;
    private boolean hasRealEstates;
    private List<RealEstate> allSystemRealEstates;
    private boolean hasPendingRequest;
    private List<SystemMessages> allPendingUserRequest;
    private List<Log> allLogs;

    public AdminPageDto(RealEstateService realEstateService, MessageService messageService, LogService logService)
            throws SQLException, NoSuchRealEstateException, NoSuchPictureException, NoSuchUserException {
        this.randomOffers = realEstateService.getRandomRealEstate();
        this.allSystemRealEstates = realEstateService.addMainPictures(realEstateService.findRealEstatesByUser("system"));
        this.hasRealEstates = setHasRealEstate();
        this.allPendingUserRequest = messageService.getAllPendingSystemRequest();
        this.hasPendingRequest = setHasPendingRequest();
        this.allLogs = logService.getLogs();
    }

    public RealEstate getRandomOffers() {
        return randomOffers;
    }

    public List<RealEstate> getAllSystemRealEstates() {
        return allSystemRealEstates;
    }

    private boolean setHasRealEstate(){
        if(allSystemRealEstates.size() > 0){
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

    public List<Log> getAllLogs() {
        return allLogs;
    }
}
