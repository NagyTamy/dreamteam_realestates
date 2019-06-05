package com.codecool.web.dto;

import com.codecool.web.model.RealEstate;
import com.codecool.web.model.Reservation;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.messages.PrivateMessages;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.model.user.Admin;
import com.codecool.web.model.user.Landlord;
import com.codecool.web.service.*;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;
import com.codecool.web.service.exception.NoSuchPictureException;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.SQLException;
import java.util.List;

public class UserProfileDto {

    private AbstractUser user;
    private List<String> asideMenu;
    private List<Comment> allreview;
    private CommentService commentService;
    private PictureService pictureService;
    private MessageService messageService;
    private List<SystemMessages> allRequest;
    private List<Comment> allSentReview;
    private List<Reservation> allReservation;
    private List<RealEstate> ownRealEstates;
    private UserService userService;
    private boolean isLoggedIn;
    private boolean isOwn;
    private boolean hasRealEstates;

    public UserProfileDto(String userName, RealEstateService realEstateService, ReservationService reservationService,
                          MessageService messageService, List<String> asideMenu, CommentService commentService, UserService userService,
                          PictureService pictureService, boolean isLoggedIn, boolean isOwn)
    throws SQLException, NoSuchUserException, NoInstanceException, NoSuchCommentException, NoSuchPictureException {
        this.user = userService.getUserByName(userName);
        user.setProfilePic(pictureService.findMainForUser(userName).getImage());
        this.commentService = commentService;
        this.pictureService = pictureService;
        this.userService = userService;
        this.asideMenu = asideMenu;
        this.allreview = setAllReview(userName);
        this.isLoggedIn = isLoggedIn;
        this.isOwn = isOwn;
        this.allRequest = messageService.filterSystemRequestsBySender(userName);
        this.allSentReview = commentService.getAllByWriter(userName);
        this.allReservation = reservationService.getAllByRenter(userName);
        this.ownRealEstates = realEstateService.findRealEstatesByUser(userName);
        this.hasRealEstates = hasRealEstates();
    }

    public AbstractUser getUser() {
        return user;
    }

    public List<String> getAsideMenu() {
        return asideMenu;
    }

    public List<Comment> getAllreview() {
        return allreview;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isOwn(){
        return isOwn;
    }

    private List<Comment> setAllReview(String userName) throws SQLException, NoInstanceException, NoSuchCommentException, NoSuchPictureException {
        allreview = commentService.getAllAboutUser(userName);
        for (Comment comment: allreview){
            AbstractUser user = userService.getUserByCommentId(comment.getId());
            user.setProfilePic(pictureService.findMainForUser(user.getName()).getImage());
            comment.setUser(user);
        }
        return allreview;
    }

    private boolean hasRealEstates(){
        return ownRealEstates.size() > 1;
    }

    public boolean isHasRealEstates() {
        return hasRealEstates;
    }

    public List<SystemMessages> getAllRequest() {
        return allRequest;
    }

    public List<Comment> getAllSentReview() {
        return allSentReview;
    }

    public List<Reservation> getAllReservation() {
        return allReservation;
    }

    public List<RealEstate> getOwnRealEstates() {
        return ownRealEstates;
    }
}
