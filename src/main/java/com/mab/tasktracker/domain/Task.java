package com.mab.tasktracker.domain;

import java.util.UUID;

public class Task {
    private final String id = UUID.randomUUID().toString();
    private String title;
    private boolean completed;
    private static String validateTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        title = title.trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        return title;
    }
    public Task(String title) {
        this.title = validateTitle(title);
        this.completed = false;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public boolean isCompleted() {
        return completed;
    }
    public String rename(String title) {
        this.title = validateTitle(title);
        return this.title;
    }
    public void markCompleted() {
        this.completed = true;
    }
    public void markIncomplete() {
        this.completed = false;
    }
}