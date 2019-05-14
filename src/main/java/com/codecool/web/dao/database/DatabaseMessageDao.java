package com.codecool.web.dao.database;

import com.codecool.web.dao.MessageDao;
import com.codecool.web.model.messages.AbstractMessage;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseMessageDao extends AbstractDao implements MessageDao {

    DatabaseMessageDao(Connection connection) {
        super(connection);
    }

    @Override
    public List<AbstractMessage> getAllByUserName(String userName) {
        return null;
    }

    @Override
    public AbstractMessage findByMessageId(int messageId) {
        return null;
    }

    @Override
    public List<AbstractMessage> getAllByRealEstate(int realEstateId) {
        return null;
    }

    @Override
    public List<AbstractMessage> getAllUnansweredForUser(String userName) {
        return null;
    }

    @Override
    public List<AbstractMessage> getAllSystemRequests() {
        return null;
    }

    @Override
    public List<AbstractMessage> getAllPendingSystemRequest() {
        return null;
    }

    @Override
    public List<AbstractMessage> filterSystemRequestsBySender(String userName) {
        return null;
    }

    @Override
    public List<AbstractMessage> filterSystemRequestByTime(LocalDateTime begins, LocalDateTime ends) {
        return null;
    }

    @Override
    public List<AbstractMessage> filterSystemRequestByType(String messageTitle) {
        return null;
    }

    @Override
    public AbstractMessage addNewMessage(AbstractMessage newMessage) {
        return null;
    }

    @Override
    public AbstractMessage removeMessage(int messageId) {
        return null;
    }
}
