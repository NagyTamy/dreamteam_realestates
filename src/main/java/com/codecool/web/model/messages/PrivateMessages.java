package com.codecool.web.model.messages;

import java.time.LocalDateTime;

public class PrivateMessages extends AbstractMessage {

    private String receiver;
    private int realEstateId = 0;


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

}
