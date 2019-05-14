package com.codecool.web.model.search;

import java.time.LocalDateTime;

public abstract class Search {

    private int id;
    private LocalDateTime time;

    Search(int id, LocalDateTime time){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
