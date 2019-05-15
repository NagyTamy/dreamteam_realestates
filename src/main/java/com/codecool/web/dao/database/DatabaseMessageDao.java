package com.codecool.web.dao.database;

import com.codecool.web.dao.MessageDao;
import com.codecool.web.model.messages.AbstractMessage;
import com.codecool.web.model.messages.PrivateMessages;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.service.exception.NoSuchMessageException;

import javax.script.ScriptContext;
import java.io.PipedReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMessageDao extends AbstractDao implements MessageDao {

    DatabaseMessageDao(Connection connection) {
        super(connection);
    }

    @Override
    public List<AbstractMessage> getAllByUserName(String userName) throws SQLException {
        List<AbstractMessage> getAllByUserName = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE sender_name=? OR receiver_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            statement.setString(2, userName);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    getAllByUserName.add(fetchMessages(resultSet));
                }
            }
        } return getAllByUserName;
    }

    @Override
    public AbstractMessage findByMessageId(int messageId) throws SQLException, NoSuchMessageException {
        String sql = "SELECT * FROM messages WHERE message_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, messageId);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return fetchMessages(resultSet);
                }
            }
        } throw new NoSuchMessageException();
    }

    @Override
    public List<AbstractMessage> getAllByRealEstate(int realEstateId) throws SQLException {
        List<AbstractMessage> allMessageByRealEstate = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE real_estate=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, realEstateId);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    allMessageByRealEstate.add(fetchMessages(resultSet));
                }
            }
        } return allMessageByRealEstate;
    }


    @Override
    public List<SystemMessages> getAllSystemRequests() throws SQLException{
        List<SystemMessages> allSystemMessages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE receiver_name='system'";
        try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)){
          while (resultSet.next()){
              allSystemMessages.add((SystemMessages)fetchMessages(resultSet));
          }
        } return allSystemMessages;
    }

    @Override
    public List<SystemMessages> getAllPendingSystemRequest() throws SQLException{
        List<SystemMessages> allPendindSystemRequest = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE receiver_name='system' AND is_answered='false'";
        try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                allPendindSystemRequest.add((SystemMessages)fetchMessages(resultSet));
            }
        } return allPendindSystemRequest;
    }

    @Override
    public List<SystemMessages> filterSystemRequestsBySender(String userName) throws SQLException {
        List<SystemMessages> allSystemRequestBySender = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE sender_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allSystemRequestBySender.add((SystemMessages) fetchMessages(resultSet));
                }
            }
        } return allSystemRequestBySender;
    }

    @Override
    public List<SystemMessages> filterSystemRequestByTime(LocalDateTime begins, LocalDateTime ends) throws SQLException {
        List<SystemMessages> systemMessagesFilteredByTime = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE date BETWEEN ? AND ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(begins));
            statement.setTimestamp(2, Timestamp.valueOf(ends));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    systemMessagesFilteredByTime.add((SystemMessages) fetchMessages(resultSet));
                }
            }
        } return systemMessagesFilteredByTime;
    }

    @Override
    public List<SystemMessages> filterSystemRequestByType(String messageTitle) throws SQLException {
        List<SystemMessages> systemMessagesFilteredByTime = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE title=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1 ,messageTitle);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    systemMessagesFilteredByTime.add((SystemMessages) fetchMessages(resultSet));
                }
            }
        } return systemMessagesFilteredByTime;
    }

    @Override
    public void addNewPrivateMessage(String sender, String receiver, int realEstate, int previousMessageId, String title, String content) throws SQLException {
        String sql = "INSERT INTO messages(sender_name, receiver_name, real_estate, history, title, content) VALUES(?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setInt(3, realEstate);
            statement.setInt(4, previousMessageId);
            statement.setString(5, title);
            statement.setString(6, content);
            executeInsert(statement);
        }
    }

    @Override
    public void addNewSystemMessage(String sender, int previousMessageId, String title, String content, int realEstate) throws SQLException {
        String sql = "INSERT INTO messages(sender_name, history, title, content, real_estate) VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, sender);
            statement.setInt(2, previousMessageId);
            statement.setString(3, title);
            statement.setString(4, content);
            statement.setInt(5, realEstate);
            executeInsert(statement);
        }
    }


    @Override
    public void removeMessage(int messageId) throws SQLException {
        String sql = "DELETE FROM messages WHERE message_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, messageId);
            executeInsert(statement);
        }
    }

    private AbstractMessage fetchMessages(ResultSet resultSet) throws SQLException{

        int id = resultSet.getInt("message_id");
        String sender = resultSet.getString("sender_name");
        String title = resultSet.getString("title");
        String message = resultSet.getString("content");
        LocalDateTime time = resultSet.getTimestamp("date").toLocalDateTime();
        int previousMessageId = resultSet.getInt("history");
        int realEstateId = resultSet.getInt("real_estate");
        AbstractMessage newMessage;

        if(resultSet.getString("receiver_name").toLowerCase().equals("system")){
            newMessage = new SystemMessages(id, sender, title, message, time, previousMessageId, realEstateId);
        } else{
            String receiver = resultSet.getString("receiver_name");
            newMessage = new PrivateMessages(id, sender, title, message, time, receiver, previousMessageId, realEstateId);
        }
        if(resultSet.getBoolean("is_answered")){
            newMessage.setAnswered(true);
        }
        return newMessage;
    }



}
