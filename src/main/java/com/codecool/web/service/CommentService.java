package com.codecool.web.service;

import com.codecool.web.dao.CommentDao;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CommentService {

    private CommentDao commentDao;

    public CommentService(CommentDao commentDao){
        this.commentDao = commentDao;
    }

    public Comment getCommentById(int id) throws SQLException, NoSuchCommentException, NoInstanceException{
        return commentDao.getCommentById(id);
    }

    public List<Comment> getAllByWriter(String userName) throws SQLException, NoInstanceException{
        return commentDao.getAllByWriter(userName);
    }

    public List<Comment> getAllAboutUser(String userName) throws SQLException, NoInstanceException{
        return commentDao.getAllAboutUser(userName);
    }

    public List<Comment> getAllAboutRealEstate(int realEstateId) throws SQLException, NoInstanceException{
        return commentDao.getAllAboutRealEstate(realEstateId);
    }

    public int avgUserRating(String userName) throws SQLException{
        return commentDao.avgUserRating(userName);
    }

    public int avgRealEstateRating(int realEstateId) throws SQLException{
        return commentDao.avgRealEstateRating(realEstateId);
    }

    public List<Comment> getAllFlagged() throws SQLException, NoInstanceException{
        return commentDao.getAllFlagged();
    }

    public void addUserComment(int reservationId, String reviewerName, String review, LocalDateTime timestamp, int userRating, String reviewedUser) throws SQLException{
        commentDao.addUserComment(reservationId, reviewerName, review, timestamp, userRating, reviewedUser);
    }

    public void addRealEstateComment(int reservationId, String reviewerName, String review, LocalDateTime timestamp, int realEstateRating, int reviewedRealEstate) throws SQLException{
        commentDao.addRealEstateComment(reservationId, reviewerName, review, timestamp, realEstateRating, reviewedRealEstate);
    }


    public void editComment(Comment editedComment) throws SQLException{
        commentDao.editComment(editedComment);
    }

    public void removeComment(int commentId) throws SQLException{
        commentDao.removeComment(commentId);
    }

    public void flagComment(int commentId) throws SQLException, NoSuchCommentException, NoInstanceException{
        commentDao.flagComment(commentId);
    }

    public boolean hasReviews(int realEstateId) throws SQLException, NoInstanceException{
        boolean hasReviews = false;
        if (commentDao.getAllAboutRealEstate(realEstateId).size() > 0){
            hasReviews = true;
        }
        return hasReviews;
    }
}
