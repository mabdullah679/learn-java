package com.mab.tasktracker.repository;
import com.mab.tasktracker.domain.Task;
import java.util.HashMap;
import java.util.Map;

public class TaskRepository {
    
    private String id;
    private Map<String, Task> tasks = new HashMap<>();
    public void createId() {
        
        int id = (int) (Math.random() * 1000);
        this.id = String.valueOf(id);
    }

    public String getId() {
        return id;
    }
    public String returnId() {
        return id;
    }

    public Task saveTask(Task task) {
        tasks.put(task.getId(), task);
        return task;}
}
