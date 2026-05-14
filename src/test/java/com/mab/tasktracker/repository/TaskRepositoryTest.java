package com.mab.tasktracker.repository;

import com.mab.tasktracker.domain.Task;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskRepositoryTest {
    
    @Test
    public void savingTaskStoresTaskById() {
        Task task = new Task("Testing task repository");
        TaskRepository repository = new TaskRepository();
        repository.saveTask(task);
        Optional<Task> foundTask = repository.findById(task.getId());
        assertTrue(foundTask.isPresent());
        assertEquals(task, foundTask.get());
    }

    @Test
    public void findByIdReturnsEmptyForMissingId() {
        TaskRepository repository = new TaskRepository();
        Optional<Task> foundTask = repository.findById("non-existent-id");
        assertTrue(foundTask.isEmpty());
    }

    @Test
    public void findAllReturnsAllSavedTasks() {
        TaskRepository repository = new TaskRepository();
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        repository.saveTask(task1);
        repository.saveTask(task2);
        List<Task> allTasks = repository.findAll();
        assertTrue(allTasks.contains(task1));
        assertTrue(allTasks.contains(task2));
        assertEquals(2, allTasks.size());
    }

    @Test
    public void deleteExistingTaskRemovesTaskFromRepository() {
        TaskRepository repository = new TaskRepository();
        Task task = new Task("Task to be deleted");
        repository.saveTask(task);
        boolean deletedTask = repository.deleteById(task.getId());
        assertTrue(deletedTask);
        assertTrue(repository.findById(task.getId()).isEmpty());
    }

    @Test
    public void deleteByIdReturnsFalseForMissingTask() {
        TaskRepository repository = new TaskRepository();
        boolean deletedTask = repository.deleteById("Nonexistent-id");
        assertFalse(deletedTask);
    }
}
