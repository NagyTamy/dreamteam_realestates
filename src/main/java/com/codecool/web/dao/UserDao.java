package com.codecool.web.dao;

import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    AbstractUser getUserByName(String userName) throws SQLException, NoSuchUserException, NoInstanceException;

    boolean isUserNameExist(String userName) throws SQLException;

    boolean isEmailExist(String email) throws SQLException;

    List<AbstractUser> getAllUsers() throws SQLException, NoInstanceException;

    List<AbstractUser> getUsersByRole(String role) throws SQLException, NoInstanceException;

    void addUser(String userName, String eMail, String password) throws SQLException;

    void updateUserData(String currentUser, String userName, String eMail, String password, String theme) throws SQLException;

    void updateUserRole(String currentUser, String userName, String role) throws SQLException;

    void removeUser(String currentUser, String userName) throws SQLException;

    AbstractUser getUserByCommentId(int commentId) throws SQLException, NoInstanceException, NoSuchCommentException;

}
