package com.codecool.web.dao.database;

import com.codecool.web.dao.CommentDao;
import com.codecool.web.model.Comment;

import java.sql.Connection;
import java.util.List;

public class DatabaseCommentDao extends AbstractDao implements CommentDao {

    DatabaseCommentDao(Connection connection) {
        super(connection);
    }

    @Override
    public Comment getCommentById(int id) {
        return null;
    }

    @Override
    public List<Comment> getAllByWriter(String userName) {
        return null;
    }

    @Override
    public List<Comment> getAllAboutUser(String userName) {
        return null;
    }

    @Override
    public List<Comment> getAllAboutRealEstate(int realEstateId) {
        return null;
    }

    @Override
    public int avgUserRating(String userName) {
        return 0;
    }

    @Override
    public int avgRealEstateRating(int realEstateId) {
        return 0;
    }

    @Override
    public List<Comment> getAllFlagged() {
        return null;
    }

    @Override
    public void addComment(Comment newComment) {

    }

    @Override
    public void editComment(Comment editedComment) {

    }

    @Override
    public void removeComment(int commentId) {

    }
}
