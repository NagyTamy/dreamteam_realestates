package com.codecool.web.model;

import com.codecool.web.model.user.Admin;

import java.time.LocalDateTime;

public class Log {

    private LocalDateTime timestamp;
    private int adminId;
    private String content;
    private Admin admin;

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
}
