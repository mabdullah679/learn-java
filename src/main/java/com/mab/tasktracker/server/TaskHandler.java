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
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        if (!path.equals("/tasks")) {
            byte[] getBytes = "Not Found".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, getBytes.length);
            exchange.getResponseBody().write(getBytes);
        } else if (!method.equalsIgnoreCase("GET")) {
            byte[] getBytes = "Method Not Allowed".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(405, getBytes.length);
            exchange.getResponseBody().write(getBytes);
        } else if (method.equalsIgnoreCase("GET") && path.equals("/tasks")) {
            List<Task> tasks = taskService.listAllTasks();
            String response = tasks.toString();
            byte[] getBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, getBytes.length);
            exchange.getResponseBody().write(getBytes);
        }
        exchange.close();
    }
}