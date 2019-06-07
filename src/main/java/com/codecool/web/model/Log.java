package com.codecool.web.model;

import com.codecool.web.model.user.Admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private LocalDateTime timestamp;
    private int adminId;
    private String content;
    private Admin admin;
    private String stringDate;
    private boolean isAdminUser;

    public Log(LocalDateTime timestamp, int adminId, String content){
        this.timestamp = timestamp;
        this.adminId = adminId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }

    public String getStringDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        stringDate = timestamp.format(formatter);
        return stringDate;
    }

    public void setAdminUser(boolean adminUser) {
        isAdminUser = adminUser;
    }

    public boolean isAdminUser() {
        return isAdminUser;
    }
}
