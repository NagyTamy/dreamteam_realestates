package com.codecool.web.dto;

import com.codecool.web.model.Picture;
import com.codecool.web.model.RealEstate;
import com.codecool.web.model.Reservation;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.*;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchRealEstateException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RealEstatePageDto {

    private CommentService commentService;
    private UserService userService;
    private PictureService pictureService;
    private RealEstate realEstate;
    private List<Picture> pictureList;
    private int realEstateId;
    private Picture mainPic;
    private boolean hasReviews;
    private boolean isLoggedIn;
    private boolean isOwn;
    private List<Reservation> availability;
    private List<Comment> allReview;

    public RealEstatePageDto(int realEstateId, CommentService commentService, UserService userService, RealEstateService realEstateService, PictureService pictureService,
                             ReservationService reservationService, boolean isLoggedIn, boolean isOwn)
            throws NoInstanceException, SQLException, NoSuchCommentException, NoSuchRealEstateException, NoSuchPictureException {

        this.realEstateId = realEstateId;
        this.userService = userService;
        this.commentService = commentService;
        this.pictureService = pictureService;

        this.realEstate = realEstateService.findRealEstateById(realEstateId);
        this.pictureList = pictureService.getAllPictureForRealEstate(realEstateId);
        this.mainPic = pictureService.findMainForRealEstate(realEstateId);
        this.hasReviews = commentService.hasReviews(realEstateId);
        this.isLoggedIn = isLoggedIn;
        this.isOwn = isOwn;
        this.availability = reservationService.getAllByRealEstate(realEstateId);
        setAllReview(realEstateId);

    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public List<Comment> getAllReview() {
        return allReview;
    }


    public List<Picture> getPictureList() {
        return pictureList;
    }

    public List<Reservation> getAvailability(){
        return availability;
    }

    public Picture getMainPic() {
        return mainPic;
    }

    public boolean getHasReviews() {
        return hasReviews;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public boolean isOwn() {
        return isOwn;
    }

    public int getRealEstateId() {
        return realEstateId;
    }

    private List<Comment> setAllReview(int realEstateId) throws SQLException, NoInstanceException, NoSuchCommentException, NoSuchPictureException {
        allReview = commentService.getAllAboutRealEstate(realEstateId);
        for (Comment comment: allReview){
            AbstractUser user = userService.getUserByCommentId(comment.getId());
            user.setProfilePic(pictureService.findMainForUser(user.getName()).getImage());
            comment.setUser(user);
        }
        return allReview;
    }

}
