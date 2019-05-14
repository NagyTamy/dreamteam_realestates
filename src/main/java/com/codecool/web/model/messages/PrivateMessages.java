package com.codecool.web.model.messages;

import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public class PrivateMessages extends AbstractMessage {

    private AbstractUser receiver;
    private int realEstateId;
    private int previousMessageId;

    public PrivateMessages(int id, AbstractUser sender, String title, String message, LocalDateTime time, AbstractUser receiver) {
        super(id, sender, title, message, time);
        this.receiver = receiver;
    }

    public AbstractUser getReceiver() {
        return receiver;
    }

    public int getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(int realEstateId) {
        this.realEstateId = realEstateId;
    }
    public void setPreviousMessageId(int previousMessage) {
        this.previousMessageId = previousMessage;
    }

    public int getPreviousMessageId() {
        return previousMessageId;
    }
}
