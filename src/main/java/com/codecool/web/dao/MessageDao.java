package com.codecool.web.dao;

import com.codecool.web.model.messages.AbstractMessage;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.service.exception.NoSuchMessageException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface MessageDao {

    /*lists all messages by user, regardless if user is sender or receiver of the message*/
    List<AbstractMessage> getAllByUserName(String userName) throws SQLException;

    AbstractMessage findByMessageId(int messageId) throws SQLException, NoSuchMessageException, NoSuchMessageException;

    List<AbstractMessage> getAllByRealEstate(int realEstateId) throws SQLException;

    List<SystemMessages> getAllSystemRequests() throws SQLException;

    List<SystemMessages> getAllPendingSystemRequest() throws SQLException;

    List<SystemMessages> filterSystemRequestsBySender(String userName) throws SQLException;

    List<SystemMessages> filterSystemRequestByTime(LocalDateTime begins, LocalDateTime ends) throws SQLException;

    List<SystemMessages> filterSystemRequestByType(String messageTitle) throws SQLException;

    void addNewPrivateMessage(String currentUser, String sender, String receiver, int realEstate, int previousMessageId, String title, String content) throws SQLException;

    void addNewSystemMessage(String currentUser, String sender, int previousMessageId, String title, String content, int realEstate) throws SQLException;

    void removeMessage(String currentUser, int messageId) throws SQLException;
}
