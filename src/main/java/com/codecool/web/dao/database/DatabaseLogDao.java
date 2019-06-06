package com.codecool.web.dao.database;

import com.codecool.web.dao.LogDao;
import com.codecool.web.model.Log;
import com.codecool.web.model.user.Admin;
import com.codecool.web.service.exception.NoSuchUserException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogDao extends AbstractDao implements LogDao {

    public DatabaseLogDao(Connection connection) {
        super(connection);
    }

    @Override
    public List<Log> getLogs() throws SQLException {
        List<Log> allLogs = new ArrayList<>();
        String sql = "SELECT * FROM logger ORDER BY date DESC";
        try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)){
            while(resultSet.next()){
                allLogs.add(new Log(resultSet.getTimestamp("date").toLocalDateTime(), resultSet.getInt("admin_id"), resultSet.getString("action_content")));
            }
        } return allLogs;
    }

    public Admin findUserByAdminId(int adminId) throws SQLException, NoSuchUserException{
        String sql = "SELECT * FROM users RIGHT JOIN admins a on users.user_name = a.user_name WHERE admin_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, adminId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    Admin user = new Admin(resultSet.getInt("admin_id"), resultSet.getString("user_name"), resultSet.getString("email"));
                    user.setAvgRating(resultSet.getFloat("avg_rating"));
                    user.setRegDate(resultSet.getTimestamp("registration_date").toLocalDateTime());
                    user.setPassword(resultSet.getString("password"));
                    return user;
                }
            }
        } throw new NoSuchUserException();
    }
}