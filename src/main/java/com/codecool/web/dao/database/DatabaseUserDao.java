package com.codecool.web.dao.database;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.model.user.Admin;
import com.codecool.web.model.user.Landlord;
import com.codecool.web.model.user.Renter;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUserDao extends AbstractDao implements UserDao {

    DatabaseUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public AbstractUser getUserByName(String userName) throws SQLException, NoSuchUserException, NoInstanceException{
        String sql = "SELECT * FROM users LEFT JOIN admins ON users.user_name = admins.user_name WHERE users.user_name=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchUser(resultSet);
                }
            }
        } throw new NoSuchUserException();
    }

    @Override
    public List<AbstractUser> getAllUsers() throws SQLException, NoInstanceException{
        List<AbstractUser> getAllUsers = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while(resultSet.next()){
                getAllUsers.add(fetchUser(resultSet));
            }
        } return getAllUsers;
    }

    @Override
    public List<AbstractUser> getUsersByRole(String role) throws SQLException, NoInstanceException{
        List<AbstractUser> getAllUsersByRole = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role_name=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, role);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    getAllUsersByRole.add(fetchUser(resultSet));
                }
            }
        } return getAllUsersByRole;
    }

    @Override
    public void addUser(String userName, String eMail, String password) throws SQLException{
        String sql = "INSERT INTO users(user_name, email, password) VALUES(?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            statement.setString(2, eMail);
            statement.setString(3, password);
            executeInsert(statement);
        }
    }

    @Override
    public void updateUserData(String userName, String eMail, String password, String theme) throws SQLException {
        String sql ="UPDATE users SET email=?, password=?, unique_theme=? WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, eMail);
            statement.setString(2, password);
            statement.setString(3, theme);
            statement.setString(4, userName);
            executeInsert(statement);
        }
    }

    @Override
    public void updateUserRole(String userName, String role) throws SQLException {
        String sql ="UPDATE users SET role_name=? WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, role);
            statement.setString(2, userName);
            executeInsert(statement);
        }
    }

    @Override
    public void removeUser(String userName) throws SQLException {
        String sql = "DELETE FROM users WHERE user_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            executeInsert(statement);
        }
    }

    private AbstractUser fetchUser(ResultSet resultSet) throws SQLException, NoInstanceException {
        String role = resultSet.getString("role_name");
        String name = resultSet.getString("user_name");
        String eMail = resultSet.getString("email");
        if(role.equals("renter")){
            return new Renter(name, eMail);
        } else if(role.equals("landlord")){
            return new Landlord(name, eMail);
        } else if (role.equals("admin")){
            int id = resultSet.getInt("admin_id");
            return new Admin(id, name, eMail);
        } else {
            throw new NoInstanceException();
        }
    }

}
