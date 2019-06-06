package com.codecool.web.service;

import com.codecool.web.dao.MessageDao;
import com.codecool.web.model.messages.AbstractMessage;
import com.codecool.web.model.messages.PrivateMessages;
import com.codecool.web.model.messages.SystemMessages;
import com.codecool.web.service.exception.NoSuchMessageException;
import org.springframework.jdbc.support.xml.SqlXmlFeatureNotImplementedException;

import javax.sound.midi.Receiver;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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

    public void addNewPrivateMessage(String currentUser, String sender, String receiver, int realEstate, int previousMessageId, String title, String content) throws SQLException{
        messageDao.addNewPrivateMessage(currentUser, sender, receiver, realEstate, previousMessageId, title, content);
    }

    public void addNewSystemMessage(String currentUser, String sender, int previousMessageId, String title, String content, int realEstate) throws SQLException{
        messageDao.addNewSystemMessage(currentUser, sender, previousMessageId, title, content, realEstate);
    }

    public void removeMessage(String currentUser, int messageId) throws SQLException{
        messageDao.removeMessage(currentUser, messageId);
    }

    private boolean hasNextMessage(int messageId) throws SQLException{
        return messageDao.hasNextMessage(messageId);
    }

    private List<PrivateMessages> getAllPrivateMessageByUser(String currentUser) throws SQLException{
        return messageDao.getAllPrivateMessageByUser(currentUser);
    }

    public boolean hasPrivateMessages(String currentUser) throws SQLException{
        return messageDao.getAllPrivateMessageByUser(currentUser).size() > 0;
    }



    private PrivateMessages findMessageByHistoryId(int previousMessageId) throws SQLException, NoSuchMessageException{
         return messageDao.findMessageByHistoryId(previousMessageId);
    }

    private int findIndex(PrivateMessages nextMessage, List<PrivateMessages> list){
        for(PrivateMessages item : list){
            if(nextMessage.getId() == item.getId()){
                return list.indexOf(item);
            }
        } return 0;
    }

    public List<LinkedList<PrivateMessages>> getMessageBatches(String currentUser) throws SQLException, NoSuchMessageException{
        List<PrivateMessages> allPrivateMessage = getAllPrivateMessageByUser(currentUser);
        List<LinkedList<PrivateMessages>> getMesageBatches = new ArrayList<>();
        int i = 0;
        while(!allPrivateMessage.isEmpty()){
            if(allPrivateMessage.get(i).getPreviousMessageId() == 0){
                PrivateMessages item = allPrivateMessage.get(i);
                LinkedList<PrivateMessages> messageBatch = new LinkedList<>();
                messageBatch.add(item);
                allPrivateMessage.remove(item);
                int z = 1;
                while(hasNextMessage(item.getId())){
                    PrivateMessages nextMessage = findMessageByHistoryId(item.getId());
                    allPrivateMessage.remove(findIndex(nextMessage, allPrivateMessage));
                    messageBatch.add(nextMessage);
                    item = nextMessage;
                    z++;
                } getMesageBatches.add(messageBatch);
                if(i < (allPrivateMessage.size()-1)){
                    i++;
                } else {
                    i = 0;
                }
            } else {
                if(i < (allPrivateMessage.size()-1)){
                    i++;
                } else {
                    i = 0;
                }
            }
        } return getMesageBatches;
    }
}
