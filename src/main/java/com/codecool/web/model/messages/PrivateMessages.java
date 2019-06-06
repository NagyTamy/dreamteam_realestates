package com.codecool.web.model.messages;

import com.codecool.web.model.RealEstate;
import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public class PrivateMessages extends AbstractMessage {

    private String receiver;
    private int realEstateId = 0;

    private AbstractUser senderUser;
    private AbstractUser receiverUser;
    private RealEstate realEstate;


    public PrivateMessages(int id, String sender, String title, String message, LocalDateTime time, String receiver, int previousMessageId, int realEstateId) {
        super(id, sender, title, message, time, previousMessageId, realEstateId);
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(int realEstateId) {
        this.realEstateId = realEstateId;
    }

    public void setSenderUser(AbstractUser senderUser) {
        this.senderUser = senderUser;
    }

    public void setRecieverUser(AbstractUser receiverUser) {
        this.receiverUser = receiverUser;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public AbstractUser getSenderUser() {
        return senderUser;
    }

    public AbstractUser getReceiverUser() {
        return receiverUser;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }
}
