package com.codecool.web.model;

import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public class Comment extends AbstractModel {

    private int reservationId;
    private AbstractUser reviewerName;
    private String review;
    private int userRating;
    private int realEstateRating;

    private LocalDateTime timestamp;
    private AbstractUser reviewedUser;
    private RealEstate reviewedRealEstate;
    private boolean isFlagged;

    Comment(int id, int reservationId, AbstractUser reviewerName, String review, int userRating, int realEstateRating) {
        super(id);
        this.reservationId = reservationId;
        this.reviewerName = reviewerName;
        this.review = review;
        this.userRating = userRating;
        this.realEstateRating = realEstateRating;
    }

    public int getReservationId() {
        return reservationId;
    }

    public AbstractUser getReviewerName() {
        return reviewerName;
    }

    public String getReview() {
        return review;
    }

    public int getRealEstateRating() {
        return realEstateRating;
    }

    public int getUserRating() {
        return userRating;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public AbstractUser getReviewedUser() {
        return reviewedUser;
    }

    public void setReviewedUser(AbstractUser reviewedUser) {
        this.reviewedUser = reviewedUser;
    }

    public RealEstate getReviewedRealEstate() {
        return reviewedRealEstate;
    }

    public void setReviewedRealEstate(RealEstate reviewedRealEstate) {
        this.reviewedRealEstate = reviewedRealEstate;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean getFlagged(){
        return isFlagged;
    }
}
