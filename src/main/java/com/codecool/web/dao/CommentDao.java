package com.codecool.web.dao;

import com.codecool.web.model.comment.Comment;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentDao {

    Comment getCommentById(int id) throws SQLException, NoSuchCommentException, NoInstanceException;

    List<Comment> getAllByWriter(String userName) throws SQLException, NoInstanceException;

    List<Comment> getAllAboutUser(String userName) throws SQLException, NoInstanceException;

    List<Comment> getAllAboutRealEstate(int realEstateId) throws SQLException, NoInstanceException;

    int avgUserRating(String userName) throws SQLException;

    int avgRealEstateRating(int realEstateId) throws SQLException;

    List<Comment> getAllFlagged() throws SQLException, NoInstanceException;

    void addUserComment(int reservationId, String reviewerName, String review, LocalDateTime timestamp, int userRating, String reviewedUser) throws SQLException;

    void addRealEstateComment(int reservationId, String reviewerName, String review, LocalDateTime timestamp, int realEstateRating, int reviewedRealEstate) throws SQLException;

    void editComment(Comment editedComment) throws SQLException;

    void removeComment(int commentId) throws SQLException;

    void flagComment(int commentId) throws SQLException, NoSuchCommentException, NoInstanceException;


}
