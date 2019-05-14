package com.codecool.web.model.comment;

import java.time.LocalDateTime;

public abstract class Comment {

    private int id;
    private int reservationId;
    private String reviewerName;
    private String review;
    private LocalDateTime timestamp;

    private boolean isFlagged;

    public Comment(int id, int reservationId, String reviewerName, String review, LocalDateTime timestamp) {
        this.id = id;
        this.reservationId = reservationId;
        this.reviewerName = reviewerName;
        this.review = review;
        this.timestamp = timestamp;
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
}
