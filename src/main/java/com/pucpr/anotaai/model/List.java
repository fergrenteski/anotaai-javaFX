package com.pucpr.anotaai.model;

import java.time.LocalDate;

public class List {
    private int id;
    private String name;
    private LocalDate creationDate;
    private int userId;

    public List() {}

    public List(int id, String name, LocalDate creationDate, int userId) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getUserId() {
        return userId;
    }

    private void setUserId(int userId) {
        this.userId = userId;
    }
}
