package com.codecool.web.service;

import com.codecool.web.model.messages.PrivateMessages;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;

public class MessageComparator implements Comparator<LinkedList<PrivateMessages>> {

    @Override
    public int compare(LinkedList<PrivateMessages> o1, LinkedList<PrivateMessages> o2) {
            LocalDateTime dateO1 = o1.get(0).getTime();
            LocalDateTime dateO2 = o2.get(0).getTime();
            return dateO1.compareTo(dateO2);
    }

}
