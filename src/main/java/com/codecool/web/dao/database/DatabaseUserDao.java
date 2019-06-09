package com.codecool.web.dao.database;

import com.codecool.web.dao.UserDao;
import com.codecool.web.model.user.AbstractUser;
import com.codecool.web.model.user.Admin;
import com.codecool.web.model.user.Landlord;
import com.codecool.web.model.user.Renter;
import com.codecool.web.service.exception.NoInstanceException;
import com.codecool.web.service.exception.NoSuchCommentException;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUserDao extends AbstractDao implements UserDao {

    public DatabaseUserDao(Connection connection) {
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
    public boolean isUserNameExist(String userName) throws SQLException {
        String sql = "SELECT * FROM users LEFT JOIN admins ON users.user_name = admins.user_name WHERE users.user_name=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return true;
                }
            }
        } return false;
    }

    @Override
    public boolean isEmailExist(String email) throws SQLException{
        String sql = "SELECT * FROM users LEFT JOIN admins ON users.user_name = admins.user_name WHERE email=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, email);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return true;
                }
            }
        } return false;
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
    public void addUser(String newUser, String eMail, String password) throws SQLException{
        String sql = "INSERT INTO users(user_name, email, password) VALUES(?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, newUser);
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
            statement.setString(2, userName);
            executeInsert(statement);
        }
    }

    @Override
    public AbstractUser getUserByCommentId(int commentId) throws SQLException, NoInstanceException, NoSuchCommentException {
        String sql = "SELECT * FROM users LEFT JOIN reviews r on users.user_name = r.reviewer_name WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, commentId);
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return fetchUser(resultSet);
                }
            }
        } throw new NoSuchCommentException();
    }


    @Override
    public List<AbstractUser> searchByUserName(String userName) throws SQLException, NoInstanceException {
        List<AbstractUser> findUsers = new ArrayList<>();
        String sql = "SELECT * FROM users LEFT JOIN admins a on users.user_name = a.user_name WHERE a.user_name=lower(?) OR a.user_name SIMILAR TO lower('%'||?) OR a.user_name SIMILAR TO lower('%'||?||'%') OR a.user_name SIMILAR TO lower(?||'%')";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            statement.setString(2, userName);
            statement.setString(3, userName);
            statement.setString(4, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                findUsers.add(fetchUser(resultSet));
            }
        } return findUsers;
    }

    private AbstractUser fetchUser(ResultSet resultSet) throws SQLException, NoInstanceException {
        AbstractUser user;
        String role = resultSet.getString("role_name");
        String name = resultSet.getString("user_name");
        String eMail = resultSet.getString("email");
        String password = resultSet.getString("password");
        if(role.equals("renter")){
            user = new Renter(name, eMail);
        } else if(role.equals("landlord")){
            user = new Landlord(name, eMail);
        } else if (role.equals("admin")){
            int id = resultSet.getInt("admin_id");
            user = new Admin(id, name, eMail);
        } else {
            throw new NoInstanceException();
        }
        user.setAvgRating(resultSet.getFloat("avg_rating"));
        user.setRegDate(resultSet.getTimestamp("registration_date").toLocalDateTime());
        user.setPassword(password);
        return user;
    }

}
