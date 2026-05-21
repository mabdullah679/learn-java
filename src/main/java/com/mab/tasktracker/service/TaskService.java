package com.mab.tasktracker.service;

import com.mab.tasktracker.domain.Task;
import com.mab.tasktracker.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository taskRepository = new TaskRepository();
    public Task createTask(String title) {
        final Task task = new Task(title);
        taskRepository.saveTask(task);
        return task;
    }

    public List<Task> listAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }
    public Optional<Task> getTask(String id) {
        return taskRepository.findById(id);
    }
    public Optional<Task> renameTask(String id, String newTitle) {
        final Optional<Task> taskOptional = getTask(id);
        if (taskOptional.isPresent()) {
            final Task task = taskOptional.get();
            task.rename(newTitle);
            return Optional.of(task);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Task> completeTask(String id) {
        final Optional<Task> taskOptional = getTask(id);
        if (taskOptional.isPresent()) {
            final Task taskPresent = taskOptional.get();
            taskPresent.markCompleted();
            return Optional.of(taskPresent);
        } else {
            return Optional.empty();
        }
    }
    public Optional<Task> reopenTask(String id) {
        final Optional<Task> taskOptional = getTask(id);
        if (taskOptional.isPresent()) { 
            final Task taskPresent = taskOptional.get();
            taskPresent.markIncomplete();
            return Optional.of(taskPresent);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteTask(String id) {
        Optional<Task> taskOptional = getTask(id);
        if (taskOptional.isPresent()) {
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

