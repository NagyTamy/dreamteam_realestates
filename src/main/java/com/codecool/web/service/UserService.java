package com.codecool.web.service;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public AbstractUser getUserByName(String userName) throws SQLException, NoSuchUserException, NoInstanceException{
        return userDao.getUserByName(userName);
    }

    public boolean isUserNameExist(String userName) throws SQLException{
        return userDao.isUserNameExist(userName);
    }

    public boolean isEmailExist(String email) throws SQLException{
        return userDao.isEmailExist(email);
    }

    public List<AbstractUser> getAllUsers() throws SQLException, NoInstanceException{
        return userDao.getAllUsers();
    }

    public List<AbstractUser> getUsersByRole(String role) throws SQLException, NoInstanceException{
        return userDao.getUsersByRole(role);
    }

    public void addUser(String userName, String eMail, String password) throws SQLException{
        userDao.addUser(userName, eMail, password);
    }

    public void updateUserData(String userName, String eMail, String password, String theme) throws SQLException{
        userDao.updateUserData(userName, eMail, password, theme);
    }

    public void updateUserRole(String userName, String role) throws SQLException{
        userDao.updateUserRole(userName, role);
    }

    public void removeUser(String userName) throws SQLException{
        userDao.removeUser(userName);
    }

    public AbstractUser getUserByCommentId(int commentId) throws SQLException, NoInstanceException, NoSuchCommentException{
        return userDao.getUserByCommentId(commentId);
    }

    public List<AbstractUser> searchByUserName(String userName) throws SQLException, NoInstanceException{
        return userDao.searchByUserName(userName);
    }

}
