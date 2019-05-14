package com.codecool.web.dao.database;

import com.codecool.web.dao.CommentDao;
import com.codecool.web.model.comment.Comment;
import com.codecool.web.model.comment.RealEstateComment;
import com.codecool.web.model.comment.UserComment;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCommentDao extends AbstractDao implements CommentDao {

    DatabaseCommentDao(Connection connection) {
        super(connection);
    }

    @Override
    public Comment getCommentById(int id) throws SQLException, NoSuchCommentException, NoInstanceException {
        String sql ="SELECT * FROM reviews WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return fetchComment(resultSet);
                }
            }
        } throw new NoSuchCommentException();
    }

    @Override
    public List<Comment> getAllByWriter(String userName) throws SQLException, NoInstanceException {
        List<Comment> allCommentByWriter = new ArrayList<>();
        String sql ="SELECT * FROM reviews WHERE reviewer_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    allCommentByWriter.add(fetchComment(resultSet));
                } return allCommentByWriter;
            }
        }
    }

    @Override
    public List<Comment> getAllAboutUser(String userName) throws SQLException, NoInstanceException {
        List<Comment> allCommentAboutUser = new ArrayList<>();
        String sql ="SELECT * FROM reviews WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    allCommentAboutUser.add(fetchComment(resultSet));
                } return allCommentAboutUser;
            }
        }
    }

    @Override
    public List<Comment> getAllAboutRealEstate(int realEstateId) throws SQLException, NoInstanceException {
        List<Comment> allCommentAboutRealEstate = new ArrayList<>();
        String sql ="SELECT * FROM reviews WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    allCommentAboutRealEstate.add(fetchComment(resultSet));
                } return allCommentAboutRealEstate;
            }
        }
    }

    @Override
    public int avgUserRating(String userName) throws SQLException{
        List<Integer> getAllRating = new ArrayList<>();
        int avgRating = 0;
        String sql ="SELECT * FROM reviews WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    getAllRating.add(resultSet.getInt("rating_user"));
                }
            }
        }
        for(Integer integer : getAllRating){
            avgRating = avgRating + integer;
        } return avgRating/getAllRating.size();
    }

    @Override
    public int avgRealEstateRating(int realEstateId) throws SQLException {
        List<Integer> getAllRating = new ArrayList<>();
        int avgRating = 0;
        String sql ="SELECT * FROM reviews WHERE real_estate_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    getAllRating.add(resultSet.getInt("rating_real_estate"));
                }
            }
        }
        for(Integer integer : getAllRating){
            avgRating = avgRating + integer;
        } return avgRating/getAllRating.size();
    }

    @Override
    public List<Comment> getAllFlagged() throws SQLException, NoInstanceException {
        List<Comment> allFlagged = new ArrayList<>();
        String sql ="SELECT * FROM reviews WHERE is_flagged='true'";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                allFlagged.add(fetchComment(resultSet));
            }
        } return allFlagged;
    }

    @Override
    public void addUserComment(int reservationId, String reviewerName, String review, LocalDateTime timestamp, int userRating, String reviewedUser) throws SQLException {

    }

    @Override
    public void addRealEstateComment(int reservationId, String reviewerName, String review, LocalDateTime timestamp, int realEstateRating, int reviewedRealEstate) throws SQLException {

    }


    @Override
    public void editComment(Comment editedComment) {

    }

    @Override
    public void removeComment(int commentId) {

    }

    private Comment fetchComment(ResultSet resultSet) throws SQLException, NoInstanceException {
        int id = resultSet.getInt("id");
        int reservationId = resultSet.getInt("reservation_id");
        String reviewername = resultSet.getString("reviewer_name");
        String review = resultSet.getString("review");
        int userRating =  resultSet.getInt("rating_user");
        int realEstateRating = resultSet.getInt("rating_real_estate");
        LocalDateTime time = resultSet.getTimestamp("date").toLocalDateTime();

        Comment comment;
        if(!resultSet.getString("user_name").isEmpty()){
            comment = new UserComment(id, reservationId, reviewername, review, time, userRating, resultSet.getString("user_name"));
            if(resultSet.getBoolean("is_flagged")){
                comment.setFlagged(true);
            } return comment;
        }
        if(resultSet.getInt("real_estate_id") != 0){
            comment = new RealEstateComment(id, reservationId, reviewername, review, time, realEstateRating, resultSet.getInt("real_estate_id"));
            if(resultSet.getBoolean("is_flagged")){
                comment.setFlagged(true);
            } return comment;
        } throw new NoInstanceException();

    }

}
