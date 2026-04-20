package com.mab.tasktracker.domain;

public class Task {
    
    // attributes
    
    private String id;
    private String title;
    private boolean completed;

    // constructor

    public Task(String id, String title) {
        this.id = id;
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