package com.codecool.web.dao.database;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.user.AbstractUser;

import java.sql.Connection;
import java.util.List;

public class DatabaseUserDao extends AbstractDao implements UserDao {

    DatabaseUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public AbstractUser getUserByName(String userName) {
        return null;
    }

    @Override
    public List<AbstractUser> getAllUsers() {
        return null;
    }

    @Override
    public List<AbstractUser> getUsersByRole(String role) {
        return null;
    }

    @Override
    public void addUser() {

    }

    @Override
    public void updateUserData(AbstractUser user) {

    }

    @Override
    public void removeUser(int userId) {

    }
}
