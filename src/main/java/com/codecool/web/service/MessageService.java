package com.codecool.web.service;

import com.codecool.web.dao.MessageDao;
import com.codecool.web.model.messages.AbstractMessage;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.service.exception.NoSuchMessageException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MessageService {

    private MessageDao messageDao;

    public MessageService(MessageDao messageDao){
        this.messageDao = messageDao;
    }

    public List<AbstractMessage> getAllByUserName(String userName) throws SQLException{
        return messageDao.getAllByUserName(userName);
    }

    public AbstractMessage findByMessageId(int messageId) throws SQLException, NoSuchMessageException, NoSuchMessageException{
        return messageDao.findByMessageId(messageId);
    }

    public List<AbstractMessage> getAllByRealEstate(int realEstateId) throws SQLException{
        return messageDao.getAllByRealEstate(realEstateId);
    }

    public List<SystemMessages> getAllSystemRequests() throws SQLException {
        return messageDao.getAllSystemRequests();
    }

    public List<SystemMessages> getAllPendingSystemRequest() throws SQLException{
        return messageDao.getAllPendingSystemRequest();
    }

    public List<SystemMessages> filterSystemRequestsBySender(String userName) throws SQLException{
        return messageDao.filterSystemRequestsBySender(userName);
    }

    public List<SystemMessages> filterSystemRequestByTime(LocalDateTime begins, LocalDateTime ends) throws SQLException{
        return messageDao.filterSystemRequestByTime(begins, ends);
    }

    public List<SystemMessages> filterSystemRequestByType(String messageTitle) throws SQLException {
        return messageDao.filterSystemRequestByType(messageTitle);
    }

    public void addNewPrivateMessage(String sender, String receiver, int realEstate, int previousMessageId, String title, String content) throws SQLException{
        messageDao.addNewPrivateMessage(sender, receiver, realEstate, previousMessageId, title, content);
    }

    public void addNewSystemMessage(String sender, int previousMessageId, String title, String content, int realEstate) throws SQLException{
        messageDao.addNewSystemMessage(sender, previousMessageId, title, content, realEstate);
    }

    public void removeMessage(int messageId) throws SQLException{
        messageDao.removeMessage(messageId);
    }
}
