package com.codecool.web.dao;

import com.codecool.web.model.messages.AbstractMessage;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public interface MessageDao {

    /*lists all messages by user, regardless if user is sender or receiver of the message*/
    List<AbstractMessage> getAllByUserName(String userName);

    AbstractMessage findByMessageId(int messageId);

    List<AbstractMessage> getAllByRealEstate(int realEstateId);

    List<AbstractMessage> getAllUnansweredForUser(String userName);

    List<AbstractMessage> getAllSystemRequests();

    List<AbstractMessage> getAllPendingSystemRequest();

    List<AbstractMessage> filterSystemRequestsBySender(String userName);

    List<AbstractMessage> filterSystemRequestByTime(LocalDateTime begins, LocalDateTime ends);

    List<AbstractMessage> filterSystemRequestByType(String messageTitle);

    AbstractMessage addNewMessage(AbstractMessage newMessage);

    AbstractMessage removeMessage(int messageId);
}
