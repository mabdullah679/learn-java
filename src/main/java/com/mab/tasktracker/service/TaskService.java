package com.mab.tasktracker.service;
import com.mab.tasktracker.domain.Task;
import com.mab.tasktracker.repository.TaskRepository;


public class TaskService {
    private Task task;
    private TaskRepository taskRepository = new TaskRepository();

    public void createTask(String title) {
        taskRepository.createId();
        String id = taskRepository.returnId();
        task = new Task(id, title);
        taskRepository.saveTask(task);
    }   
}
