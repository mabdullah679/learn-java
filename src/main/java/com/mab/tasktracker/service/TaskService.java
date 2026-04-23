package com.mab.tasktracker.service;
import com.mab.tasktracker.domain.Task;
import com.mab.tasktracker.repository.TaskRepository;


public class TaskService {
    private TaskRepository taskRepository = new TaskRepository();


    public Task createTask(String title) {
        final Task task = new Task(title);
        taskRepository.saveTask(task);
        return task;
    }
}
