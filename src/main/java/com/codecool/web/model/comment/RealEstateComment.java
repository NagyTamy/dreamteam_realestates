package com.codecool.web.model.comment;


import com.codecool.web.model.RealEstate;

import java.time.LocalDateTime;

public class RealEstateComment extends Comment {

    private int realEstateRating;
    private int reviewedRealEstate;
    private RealEstate realEstate;


    public RealEstateComment(int id, int reservationId, String reviewerName, String review, LocalDateTime timestamp, int realEstateRating, int reviewedRealEstate) {
        super(id, reservationId, reviewerName, review, timestamp);
        this.realEstateRating = realEstateRating;
        this.reviewedRealEstate = reviewedRealEstate;
        setHasRealEstate(true);
    }

    public int getReviewedRealEstate() {
        return reviewedRealEstate;
    }

    public int getRealEstateRating() {
        return realEstateRating;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }


}
