package com.codecool.web.model.comment;


import java.time.LocalDateTime;

public class RealEstateComment extends Comment {

    private int realEstateRating;
    private int reviewedRealEstate;

    public RealEstateComment(int id, int reservationId, String reviewerName, String review, LocalDateTime timestamp, int realEstateRating, int reviewedRealEstate) {
        super(id, reservationId, reviewerName, review, timestamp);
        this.realEstateRating = realEstateRating;
        this.reviewedRealEstate = reviewedRealEstate;
    }

    public int getReviewedRealEstate() {
        return reviewedRealEstate;
    }

    public int getRealEstateRating() {
        return realEstateRating;
    }
}
