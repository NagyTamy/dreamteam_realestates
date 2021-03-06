package com.codecool.web.model.messages;

import com.codecool.web.model.RealEstate;
import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public class PrivateMessages extends AbstractMessage {

    private String receiver;

    private AbstractUser senderUser;
    private AbstractUser receiverUser;
    private RealEstate realEstate;
    private boolean iAmSender = false;
    private boolean iAmReceiver = false;


    public PrivateMessages(int id, String sender, String title, String message, LocalDateTime time, String receiver, int previousMessageId, int realEstateId) {
        super(id, sender, title, message, time, previousMessageId, realEstateId);
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
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

    public void setiAmReceiver(boolean iAmReceiver) {
        this.iAmReceiver = iAmReceiver;
    }

    public void setiAmSender(boolean iAmSender) {
        this.iAmSender = iAmSender;
    }

    public boolean isiAmReceiver() {
        return iAmReceiver;
    }

    public boolean isiAmSender() {
        return iAmSender;
    }
}
