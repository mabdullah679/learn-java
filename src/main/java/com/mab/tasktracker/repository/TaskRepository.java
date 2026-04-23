package com.mab.tasktracker.repository;
import com.mab.tasktracker.domain.Task;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TaskRepository {


    private Map<String, Task> tasks = new HashMap<>();

    public Task saveTask(Task task) {
        tasks.put(task.getId(), task);
        return task;}


    public Optional<Task> findById(String id) {

        if (tasks.containsKey(id)) {

            final Task task = tasks.get(id);
            return Optional.of(task);

        }
        else {
            return Optional.empty();
        }
    }
}
