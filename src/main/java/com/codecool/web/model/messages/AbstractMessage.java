package com.codecool.web.model.messages;

import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public abstract class AbstractMessage {

    private int id;
    private AbstractUser sender;
    private String title;
    private String message;
    private LocalDateTime time;

    public AbstractMessage(int id, AbstractUser sender, String title, String message, LocalDateTime time){
        this.id = id;
        this.sender = sender;
        this.title = title;
        this.message = message;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public AbstractUser getSender() {
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
}
