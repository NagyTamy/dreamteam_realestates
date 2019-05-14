package com.codecool.web.dao;

import com.codecool.web.model.Comment;

import java.util.List;

public interface CommentDao {

    Comment getCommentById(int id);

    List<Comment> getAllByWriter(String userName);

    List<Comment> getAllAboutUser(String userName);

    List<Comment> getAllAboutRealEstate(int realEstateId);

    int avgUserRating(String userName);

    int avgRealEstateRating(int realEstateId);

    List<Comment> getAllFlagged();

    void addComment(Comment newComment);

    void editComment(Comment editedComment);

    void removeComment(int commentId);

}
