package com.codecool.web.service;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.service.exception.NoInstanceException;
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

    public List<AbstractUser> getAllUsers() throws SQLException, NoInstanceException{
        return userDao.getAllUsers();
    }

    public List<AbstractUser> getUsersByRole(String role) throws SQLException, NoInstanceException{
        return userDao.getUsersByRole(role);
    }

    public void addUser(String currentuser, String userName, String eMail, String password) throws SQLException{
        userDao.addUser(currentuser, userName, eMail, password);
    }

    public void updateUserData(String currentuser, String userName, String eMail, String password, String theme) throws SQLException{
        userDao.updateUserData(currentuser, userName, eMail, password, theme);
    }

    public void updateUserRole(String currentuser, String userName, String role) throws SQLException{
        userDao.updateUserRole(currentuser, userName, role);
    }

    public void removeUser(String currentuser, String userName) throws SQLException{
        userDao.removeUser(currentuser, userName);
    }

}
