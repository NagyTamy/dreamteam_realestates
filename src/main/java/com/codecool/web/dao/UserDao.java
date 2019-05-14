package com.codecool.web.dao;

import com.codecool.web.model.user.AbstractUser;

import java.util.List;

public interface UserDao {

    AbstractUser getUserByName(String userName);

    List<AbstractUser> getAllUsers();

    List<AbstractUser> getUsersByRole(String role);

    void addUser();

    void updateUserData(AbstractUser user);

    void removeUser(int userId);

}
