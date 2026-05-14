package com.mab.tasktracker.service;

import com.mab.tasktracker.domain.Task;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class TaskServiceTest {

    @Test
    public void createTaskCreatesAndStoresTask() {
        TaskService service = new TaskService();
        Task createdTask = service.createTask("New task");
        Optional<Task> foundTask = service.findById(createdTask.getId());
        assertTrue(foundTask.isPresent());
        assertEquals("New task", createdTask.getTitle());
        assertEquals(createdTask, foundTask.get());
    }

    @Test
    public void listAllTasksReturnsAllCreatedTasks() {
        TaskService service = new TaskService();
        Task task1 = service.createTask("Task 1");
        Task task2 = service.createTask("Task 2");
        List<Task> allTasks = service.listAllTasks();
        assertTrue(allTasks.contains(task1));
        assertTrue(allTasks.contains(task2));
        assertEquals(2, allTasks.size());
    }

    @Test
    public void findByIdReturnsEmptyForMissingTask() {
        TaskService service = new TaskService();
        Optional<Task> foundTask = service.findById("x123");
        assertTrue(foundTask.isEmpty());
    }

    @Test
    public void renameTaskRenamesExistingTask() {
        TaskService service = new TaskService();
        Task task = service.createTask("Name");
        Optional<Task> renamedTask = service.renameTask(task.getId(), "New Name");
        assertTrue(renamedTask.isPresent());
        assertEquals("New Name", renamedTask.get().getTitle());
    }

    @Test
    public void renameTaskReturnsEmptyForMissingTask() {
        TaskService service = new TaskService();
        Optional<Task> renamedTask = service.renameTask("x123", "New Name");
        assertTrue(renamedTask.isEmpty());
    }

    @Test
    public void completeTaskMarksExistingTaskAsCompleted() {
        TaskService service = new TaskService();
        Task task = service.createTask("Task");
        Optional<Task> completedTask = service.completeTask(task.getId());
        assertTrue(completedTask.isPresent());
        assertTrue(completedTask.get().isCompleted());
    }

    @Test
    public void completeTaskReturnsEmptyForMissingTask() {
        TaskService service = new TaskService();
        Optional<Task> completedTask = service.completeTask("x123");
        assertTrue(completedTask.isEmpty());
    }

    @Test
    public void reopenTaskMarksExistingTaskAsIncomplete() {
        TaskService service = new TaskService();
        Task task = service.createTask("Task");
        service.completeTask(task.getId());
        Optional<Task> reopenedTask = service.reopenTask(task.getId());
        assertTrue(reopenedTask.isPresent());
        assertFalse(reopenedTask.get().isCompleted());
    }

    @Test
    public void reopenTaskReturnsEmptyForMissingTask() {
        TaskService service = new TaskService();
        Optional<Task> reopenedTask = service.reopenTask("x123");
        assertTrue(reopenedTask.isEmpty());
    }

    @Test
    public void deleteTaskDeletesExistingTask() {
        TaskService service = new TaskService();
        Task task = service.createTask("Task");
        boolean deletedTask = service.deleteTask(task.getId());
        assertTrue(deletedTask);
        Optional<Task> foundTask = service.findById(task.getId());
        assertTrue(foundTask.isEmpty());
    }

    @Test
    public void deleteTaskReturnsFalseForMissingTask() {
        TaskService service = new TaskService();
        assertFalse(service.deleteTask("x123"));
    }
}