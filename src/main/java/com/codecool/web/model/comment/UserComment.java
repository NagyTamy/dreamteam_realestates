package com.codecool.web.model.comment;

import java.time.LocalDateTime;

public class UserComment extends Comment {

    private int userRating;
    private String reviewedUser;


    public UserComment(int id, String reviewerName, String review, LocalDateTime timestamp, int userRating, String reviewedUser) {
        super(id, reviewerName, review, timestamp);
        this.userRating = userRating;
        this.reviewedUser = reviewedUser;
        setHasRealEstate(false);
    }

    public String getReviewedUser() {
        return reviewedUser;
    }

    public int getUserRating() {
        return userRating;
    }

    public boolean hasRealEstate() {
        return false;
    }
}
