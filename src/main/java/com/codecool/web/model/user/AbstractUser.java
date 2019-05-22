package com.codecool.web.model.user;

import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.Reservation;
import com.codecool.web.model.messages.PrivateMessages;
import com.codecool.web.model.RealEstate;

import java.time.LocalDateTime;
import java.util.List;


public abstract class AbstractUser {

    private String name;
    private String eMail;
    private String password;
    private String theme;
    private LocalDateTime regDate;
    private byte[] profilePic;
    private List<RealEstate> favourites;
    private List<PrivateMessages> messages;
    private List<Reservation> reservations;
    private List<Comment> comments;
    private float avgRating;


    AbstractUser(String name, String eMail) {
        this.name = name;
        this.eMail = eMail;
    }


    public String getName() {
        return name;
    }

    public String geteMail() {
        return eMail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public List<RealEstate> getFavourites() {
        return favourites;
    }

    public void setFavourites(RealEstate realEstate) {
        favourites.add(realEstate);
    }

    public void setFavourites(List<RealEstate> favourites) {
        this.favourites = favourites;
    }

    public List<PrivateMessages> getMessages() {
        return messages;
    }

    public void setMessages(PrivateMessages message) {
        messages.add(message);
    }

    public void setMessages(List<PrivateMessages> messages) {
        this.messages = messages;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Reservation reservation) {
        reservations.add(reservation);
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(Comment comment) {
        comments.add(comment);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

}
