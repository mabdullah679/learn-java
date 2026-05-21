package com.mab.tasktracker.server;

import com.mab.tasktracker.domain.Task;
import com.mab.tasktracker.service.TaskService;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
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

    private List<String> recognizedEndpoints() {
        List<String> validEndpoints = new ArrayList<>();
        validEndpoints.add("GET /tasks - List all tasks");
        validEndpoints.add("POST /tasks - Create a new task with the title in the request body");
        validEndpoints.add("PUT /tasks - Update task title");
        return validEndpoints;
    }

    
    private String buildNotFoundMessage(String path) {

        StringBuilder sb = new StringBuilder();
        sb.append("Endpoint '").append(path).append("' not found.\n");
        sb.append("Valid endpoints are:\n");
        for (String endpoint : recognizedEndpoints()) {
            sb.append("- ").append(endpoint).append("\n");
        }
        return sb.toString();
    }
    private String extractTaskId(String path) {
        String[] parts = path.split("/");
        if (parts.length != 3) {
            return null;
        }
        if (parts[1].equals("tasks") && parts[2] != null && !parts[2].isBlank()) {
            return parts[2];
        } else {
            return null;
        }
    }

    private String getRequestBody(HttpExchange exchange) throws IOException{
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8).trim();
        return requestBody;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String taskId = extractTaskId(path);
        if (path.equals("/tasks")) {
            switch (method) {
                case "POST": {
                    String requestBody = getRequestBody(exchange);
                    try {
                        Task returnedTask = taskService.createTask(requestBody);
                        sendResponse(exchange, 201, formatTask(returnedTask));
                    } catch (IllegalArgumentException e) {
                        sendResponse(exchange, 400, e.getMessage());
                        
                    }
                    return;
                }
                case "GET": {       
                    ArrayList<Task> tasks = new ArrayList<>(taskService.listAllTasks());
                    if (tasks.isEmpty()) {
                        sendResponse(exchange, 200, "No tasks found.");
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (Task task : tasks) {
                        sb.append(formatTask(task));
                    }
                    sendResponse(exchange, 200, sb.toString());
                    return;
                }
                default: {
                    sendResponse(exchange, 405, "ERROR CODE 405: Forbidden Method");
                    return;
                }
            }
        } else if (taskId != null) {
            switch(method) {
                case "PUT": {
                    try {
                        String requestBody = getRequestBody(exchange);
                        Optional<Task> taskOptional = taskService.renameTask(taskId, requestBody);
                        if (taskOptional.isPresent()) {
                            sendResponse(exchange, 200, formatTask(taskOptional.get()));
                            return;
                        }   else {
                            sendResponse(exchange, 404, "ERROR CODE 404: Task not found");
                            return;
                        }
                    } catch (IllegalArgumentException e) {
                        sendResponse(exchange, 400, e.getMessage());
                        return;
                    } 
                }
                default: {
                    sendResponse(exchange, 405, ("Forbidden Method"));
                } return;
            }
        }
        else {
            sendResponse(exchange, 404, buildNotFoundMessage(path));
        }
    }
}