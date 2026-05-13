package com.mab.tasktracker.server;

import com.mab.tasktracker.service.TaskService;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class TaskHandler implements HttpHandler {
    private final TaskService taskService;

    public TaskHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    response = taskService.listAllTasks().toString();
                    break;
                default:
                    statusCode = 405; // Method Not Allowed
                    response = "Method Not Allowed";
            }
        } catch (Exception e) {
            statusCode = 500; // Internal Server Error
            response = "Internal Server Error";
        } finally {
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }
}