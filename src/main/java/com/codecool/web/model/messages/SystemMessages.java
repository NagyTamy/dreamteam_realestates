package com.codecool.web.model.messages;

import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public class SystemMessages extends AbstractMessage {

    private int previousMessageId;

    public SystemMessages(int id, AbstractUser sender, String title, String message, LocalDateTime time) {
        super(id, sender, title, message, time);
        final String receiver = "system";
    }

    public void setPreviousMessageId(int previousMessage) {
        this.previousMessageId = previousMessage;
    }

    public int getPreviousMessageId() {
        return previousMessageId;
    }

}
