package com.codecool.web.model.messages;

import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public abstract class AbstractMessage {

    private int id;
    private String sender;
    private String title;
    private String message;
    private LocalDateTime time;
    private int previousMessageId;
    private int realEstateId;

    private boolean isAnswered;

    public AbstractMessage(int id, String sender, String title, String message, LocalDateTime time, int previousMessageId, int realEstateId){
        this.id = id;
        this.sender = sender;
        this.title = title;
        this.message = message;
        this.time = time;
        this.previousMessageId = previousMessageId;
        this.realEstateId = realEstateId;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setAnswered(boolean isAnswered) {
        isAnswered = isAnswered;
    }

    public boolean getIsAnswered(){
        return isAnswered;
    }

    public int getPreviousMessageId() {
        return previousMessageId;
    }
}
