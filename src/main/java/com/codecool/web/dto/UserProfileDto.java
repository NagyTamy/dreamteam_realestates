package com.codecool.web.dto;

import com.codecool.web.model.RealEstate;
import com.codecool.web.model.Reservation;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.comment.RealEstateComment;
import com.codecool.web.model.comment.UserComment;
import com.codecool.web.model.messages.PrivateMessages;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.model.user.AbstractUser;

import com.codecool.web.service.*;
import com.codecool.web.service.exception.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UserProfileDto {

    private AbstractUser user;
    private List<String> asideMenu;
    private List<Comment> allReview;
    private CommentService commentService;
    private PictureService pictureService;
    private MessageService messageService;
    private RealEstateService realEstateService;
    private ReservationService reservationService;
    private List<SystemMessages> allRequest;
    private List<Comment> allSentReview;
    private List<Reservation> allPastReservation;
    private Reservation currentReservation;
    private List<Reservation> allUpcomingReservation;
    private List<RealEstate> ownRealEstates;
    private UserService userService;
    private List<LinkedList<PrivateMessages>> messageBatches;
    private boolean hasPrivateMessages;
    private boolean isLoggedIn;
    private boolean isOwn;
    private boolean hasRealEstates;
    private boolean hasCurrentReservation;
    private boolean hasReviews;

    public UserProfileDto(String userName, RealEstateService realEstateService, ReservationService reservationService,
                          MessageService messageService, List<String> asideMenu, CommentService commentService, UserService userService,
                          PictureService pictureService, boolean isLoggedIn, boolean isOwn)
    throws SQLException, NoSuchUserException, NoInstanceException, NoSuchCommentException, NoSuchPictureException, NoSuchRealEstateException, NoSuchMessageException {
        this.user = userService.getUserByName(userName);
        user.setPic(pictureService.findMainForUser(userName).getImage());
        this.commentService = commentService;
        this.messageService = messageService;
        this.reservationService = reservationService;
        this.pictureService = pictureService;
        this.realEstateService = realEstateService;
        this.userService = userService;
        this.asideMenu = asideMenu;
        this.allReview = setAllReview(userName);
        this.isLoggedIn = isLoggedIn;
        this.isOwn = isOwn;
        this.allRequest = messageService.filterSystemRequestsBySender(userName);
        this.allSentReview = setSentReviews(userName);
        this.allPastReservation = setRealEstateToReservationList(reservationService.getAllPastByRenter(userName));
        this.hasCurrentReservation = hasCurrentReservation(userName);
        if (hasCurrentReservation){
            this.currentReservation = setRealEstateToSingleReservation(reservationService.getCurrentByRenter(userName));
        }
        this.allUpcomingReservation = setRealEstateToReservationList(reservationService.getAllUpcomingByRenter(userName));
        this.ownRealEstates = setPicturesToRealEstateList(userName);
        this.hasRealEstates = hasRealEstates();
        this.hasPrivateMessages = checkPrivateMessages(userName);
        if(hasPrivateMessages){
            List<LinkedList<PrivateMessages>> messageBatches =  setMessageBatchForUser(userName);
            messageBatches.sort(new MessageComparator().reversed());
            this.messageBatches = messageBatches;
        }
        this.hasReviews = hasReviews();
    }

    public AbstractUser getUser() {
        return user;
    }

    public List<String> getAsideMenu() {
        return asideMenu;
    }

    public List<Comment> getAllReview() {
        return allReview;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isOwn(){
        return isOwn;
    }

    private List<Comment> setAllReview(String userName) throws SQLException, NoInstanceException, NoSuchCommentException, NoSuchPictureException {
        allReview = commentService.getAllAboutUser(userName);
        for (Comment comment: allReview){
            AbstractUser user = userService.getUserByCommentId(comment.getId());
            user.setPic(pictureService.findMainForUser(user.getName()).getImage());
            comment.setUser(user);
        }
        return allReview;
    }

    private List<Comment> setSentReviews(String userName) throws SQLException, NoInstanceException, NoSuchRealEstateException, NoSuchPictureException, NoSuchCommentException, NoSuchUserException {
        allSentReview = commentService.getAllByWriter(userName);
        for (Comment comment : allSentReview){
            if(comment instanceof RealEstateComment){
                int realEstateId = ((RealEstateComment) comment).getReviewedRealEstate();
                RealEstate realEstate = realEstateService.findRealEstateById(realEstateId);
                realEstate.setPic(pictureService.findMainForRealEstate(realEstateId).getImage());
                ((RealEstateComment) comment).setRealEstate(realEstate);
            } else {
                AbstractUser user = userService.getUserByName(((UserComment) comment).getReviewedUser());
                user.setPic(pictureService.findMainForUser(userName).getImage());
                comment.setReviewedUser(user);
            }
        }
        return allSentReview;
    }

    private List<Reservation> setRealEstateToReservationList(List<Reservation> list) throws SQLException, NoSuchRealEstateException, NoSuchPictureException{
        for (Reservation reservation : list){
            reservation.setRealEstate(realEstateService.findRealEstateById(reservation.getRealEstateId()));
            reservation.getRealEstate().setPic(pictureService.findMainForRealEstate(reservation.getRealEstateId()).getImage());
        } return list;
    }

    private Reservation setRealEstateToSingleReservation(Reservation reservation) throws SQLException, NoSuchRealEstateException, NoSuchPictureException{
        reservation.setRealEstate(realEstateService.findRealEstateById(reservation.getRealEstateId()));
        reservation.getRealEstate().setPic(pictureService.findMainForRealEstate(reservation.getRealEstateId()).getImage());
        return reservation;
    }

    private List<RealEstate> setPicturesToRealEstateList(String userName) throws SQLException, NoSuchPictureException{
        ownRealEstates = realEstateService.findRealEstatesByUser(userName);
        for (RealEstate realEstate : ownRealEstates){
            realEstate.setPic(pictureService.findMainForRealEstate(realEstate.getId()).getImage());
        } return ownRealEstates;
    }

    private boolean hasRealEstates(){
        return ownRealEstates.size() > 0;
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

    private boolean hasCurrentReservation(String userName) throws SQLException{
        if (reservationService.getCurrentByRenter(userName) != null){
            return true;
        } else {
            return false;
        }
    }

    public List<RealEstate> getOwnRealEstates() {
        return ownRealEstates;
    }

    public boolean isHasCurrentReservation() {
        return hasCurrentReservation;
    }

    public List<Reservation> getAllPastReservation() {
        return allPastReservation;
    }

    public List<Reservation> getAllUpcomingReservation() {
        return allUpcomingReservation;
    }

    public Reservation getCurrentReservation() {
        return currentReservation;
    }

    private boolean checkPrivateMessages(String userName) throws SQLException{
        return messageService.hasPrivateMessages(userName);
    }

    private void setUsersAndRealEstateToMessageList (LinkedList<PrivateMessages> list, String currentUser) throws SQLException, NoSuchUserException, NoSuchPictureException, NoInstanceException, NoSuchRealEstateException{
        for (PrivateMessages privateMessage : list) {
            String receiverName = privateMessage.getReceiver();
            AbstractUser receiverUser = userService.getUserByName(receiverName);
            receiverUser.setPic(pictureService.findMainForUser(receiverName).getImage());
            privateMessage.setRecieverUser(receiverUser);
            String senderName = privateMessage.getSender();
            AbstractUser senderUser = userService.getUserByName(senderName);
            senderUser.setPic(pictureService.findMainForUser(senderName).getImage());
            privateMessage.setSenderUser(senderUser);
            if(receiverName.equals(currentUser)){
                privateMessage.setiAmReceiver(true);
            } else {
                privateMessage.setiAmSender(true);
            }
            if(privateMessage.getHasRealEstate()){
                RealEstate realEstate = realEstateService.findRealEstateById(privateMessage.getRealEstateId());
                realEstate.setPic(pictureService.findMainForRealEstate(privateMessage.getRealEstateId()).getImage());
                privateMessage.setRealEstate(realEstate);
            }
        }
    }

    private List<LinkedList<PrivateMessages>> setMessageBatchForUser(String currentUser) throws SQLException, NoSuchPictureException, NoSuchMessageException, NoSuchUserException, NoInstanceException, NoSuchRealEstateException{
        List<LinkedList<PrivateMessages>> setMessageBatchForUser = messageService.getMessageBatches(currentUser);
        for(LinkedList<PrivateMessages> item : setMessageBatchForUser){
            setUsersAndRealEstateToMessageList(item, currentUser);
        } return setMessageBatchForUser;
    }

    public List<LinkedList<PrivateMessages>> getMessageBatches() {
        return messageBatches;
    }

    public boolean isHasPrivateMessages() {
        return hasPrivateMessages;
    }

    private boolean hasReviews(){
        if (allReview.size() > 0){
            hasReviews = true;
        }
        else {
            hasReviews = false;
        } return hasReviews;
    }

    public boolean isHasReviews() {
        return hasReviews;
    }
}
