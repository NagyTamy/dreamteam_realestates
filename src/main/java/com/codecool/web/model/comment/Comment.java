package com.codecool.web.model.comment;

import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Comment {

    private int id;
    private int reservationId;
    private String reviewerName;
    private String review;
    private LocalDateTime timestamp;
    private AbstractUser user; /*reviewer*/
    private AbstractUser reviewedUser;
    private String timeStampString;
    private boolean hasRealEstate;

    private boolean isFlagged = false;

    public Comment(int id, int reservationId, String reviewerName, String review, LocalDateTime timestamp) {
        this.id = id;
        this.reservationId = reservationId;
        this.reviewerName = reviewerName;
        this.review = review;
        this.timestamp = timestamp;
        timeStampString = timeStampToString(timestamp);
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReview() {
        return review;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean getFlagged(){
        return isFlagged;
    }

    public int getId() {
        return id;
    }

    public void setUser(AbstractUser user) {
        this.user = user;
    }

    public AbstractUser getUser() {
        return user;
    }

    private String timeStampToString(LocalDateTime timestamp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return timestamp.format(formatter);
    }

    public String getTimeStampString() {
        return timeStampString;
    }

    public void setReviewedUser(AbstractUser reviewedUser) {
        this.reviewedUser = reviewedUser;
    }

    public AbstractUser getReviewedUserInstance() {
        return reviewedUser;
    }

    public void setHasRealEstate(boolean hasRealEstate) {
        this.hasRealEstate = hasRealEstate;
    }

    public boolean isHasRealEstate() {
        return hasRealEstate;
    }
}
