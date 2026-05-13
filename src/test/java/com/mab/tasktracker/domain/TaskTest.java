package com.mab.tasktracker.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {
    @Test
    public void storesTitleWhenTaskIsCreated() {
        String title = "Task 1: Testing Class Functionality";
        Task task = new Task(title);
        assertEquals(title, task.getTitle());
    }

    @Test
    public void trimsTitleWhenTaskIsCreated() {
        String title = "    Task 1: Testing Trimming behavior    ";
        Task task = new Task(title);
        String trimmedTitle = title.trim();
        assertEquals(trimmedTitle, task.getTitle());
    }

    @Test
    public void newTaskStartsAsIncomplete() {
        Task task = new Task("Task 1: Testing Task Completion Status");
        assertFalse(task.isCompleted());
    }

    @Test
    public void idIsNotNullAndNotBlank() {
        Task task = new Task("Task 1: Testing ID Behavior");
        String id = task.getId();
        assertNotNull(id);
        assertFalse(id.isBlank());
    }

    @Test
    public void rejectNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Task(null));
    }

    @Test
    public void rejectEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Task(""));
    }

    @Test
    public void rejectSpaceOnlyTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Task("  "));
    }

    @Test
    public void renameChangesTaskTitle() {
        Task task = new Task("Task 1: Testing Rename Functionality");
        task.rename("Task 1: Renamed Task");
        assertEquals("Task 1: Renamed Task", task.getTitle());
    }

    @Test
    public void renameTrimsTitle() {
        Task task = new Task("Task 1: Testing Rename Trim Functionality");
        task.rename("    Task 1: Renamed Task    ");
        assertEquals("Task 1: Renamed Task", task.getTitle());
    }

    @Test
    public void renameRejectsNullTitle() {
        Task task = new Task("Task 1: Testing Rename Null Title Rejection");
        assertThrows(IllegalArgumentException.class, () -> task.rename(null));
    }

    @Test
    public void renameRejectsEmptyTitle() {
        Task task = new Task("Task 1: Testing Empty Title Rename Rejection");
        assertThrows(IllegalArgumentException.class, () -> task.rename(""));
    }

    @Test
    public void renameRejectsSpaceOnlyTitle() {
        Task task = new Task("Task 1: Testing Space Only Title Rename Rejection");
        assertThrows(IllegalArgumentException.class, () -> task.rename("   "));
    }

    @Test
    public void markCompletedWorks() {
        Task task = new Task("Task 1: Testing Mark Completed Functionality");
        task.markCompleted();
        assertTrue(task.isCompleted());
    }

    @Test
    public void markIncompleteWorks() {
        Task task = new Task("Task 1: Testing Mark Incomplete Functionality");
        task.markCompleted();
        task.markIncomplete();
        assertFalse(task.isCompleted());
    }
}
