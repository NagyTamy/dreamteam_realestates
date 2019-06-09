package com.codecool.web.dto;

import com.codecool.web.model.Log;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.*;
import com.codecool.web.service.exception.*;

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
    private List<String> asideMenu = new ArrayList<>();
    private boolean hasReportedReview;
    private List<Comment> reportedReviews;


    public AdminPageDto(RealEstateService realEstateService, MessageService messageService, LogService logService, CommentService commentService)
            throws SQLException, NoSuchRealEstateException, NoSuchPictureException, NoSuchUserException, NoInstanceException {
        this.randomOffer = realEstateService.getRandomRealEstate();
        this.ownRealEstates = realEstateService.addMainPictures(realEstateService.findRealEstatesByUser("system"));
        this.hasRealEstates = setHasRealEstate();
        this.allPendingUserRequest = messageService.getAllPendingSystemRequest();
        this.hasPendingRequest = setHasPendingRequest();
        this.allLogs = logService.getLogs();
        this.reportedReviews = commentService.getAllFlagged();
        this.hasReportedReview = hasReportedReview();
        initAsideMenu();
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

    private boolean hasReportedReview(){
        return reportedReviews.size() > 0;
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

    private List<String> initAsideMenu(){
        asideMenu.add("Logs");
        asideMenu.add("System real estates");
        if(hasPendingRequest) {
            asideMenu.add("Requests");
        }
        if(hasReportedReview){
            asideMenu.add("Flagged comments");
        }
        return asideMenu;
    }

    public boolean isHasReportedReview() {
        return hasReportedReview;
    }

    public List<Comment> getReportedReviews() {
        return reportedReviews;
    }

}
