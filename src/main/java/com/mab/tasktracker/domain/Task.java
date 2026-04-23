package com.mab.tasktracker.domain;

public class Task {
    
    // attributes
    
    private String id = java.util.UUID.randomUUID().toString();
    private String title;
    private boolean completed;

    // constructor

    public Task(String title) {
        this.title = title;
    }

    // getters

    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
    
    // setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void markCompleted() {
        this.completed = true;
    }

}