package com.codecool.web.model.messages;

import com.codecool.web.model.RealEstate;
import com.codecool.web.model.user.AbstractUser;

import java.time.LocalDateTime;

public class SystemMessages extends AbstractMessage {

    private final String receiver = "system";



    public SystemMessages(int id, String sender, String title, String message, LocalDateTime time, int previousMessageId, int realEstateId) {
        super(id, sender, title, message, time, previousMessageId, realEstateId);
    }

    public String getReceiver() {
        return receiver;
    }

}

