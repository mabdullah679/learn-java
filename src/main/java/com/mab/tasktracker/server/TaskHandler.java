package com.mab.tasktracker.server;

import com.mab.tasktracker.domain.Task;
import com.mab.tasktracker.service.TaskService;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private final TaskService taskService;
    public TaskHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    private String formatTask(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(task.getId()).append("\n");
        sb.append("Title: ").append(task.getTitle()).append("\n");
        sb.append("Completed: ").append(task.isCompleted() ? "Yes" : "No").append("\n");
        sb.append("-----\n");
        return sb.toString();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        StringBuilder sb = new StringBuilder();
        List<Task> tasks = taskService.listAllTasks();
        for (Task task : tasks) {
            sb.append(formatTask(task));
        }
        if (!path.equals("/tasks")) {
            sendResponse(exchange, 404, "Not Found");
            return;
        } else if (path.equals("/tasks") && method.equalsIgnoreCase("POST")) {
            byte[] title = exchange.getRequestBody().readAllBytes();
            String thisTitle = new String(title, StandardCharsets.UTF_8).trim();
            Task newTask = taskService.createTask(thisTitle);
            sendResponse(exchange, 201, formatTask(newTask));
            return;
        } else if (!method.equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }
        if (tasks.isEmpty()) {
            sendResponse(exchange, 200, "No tasks found.");
            return;
        } else if (!tasks.isEmpty() && method.equalsIgnoreCase("GET")) {
            sendResponse(exchange, 200, sb.toString());
                return;
        }
    }
}