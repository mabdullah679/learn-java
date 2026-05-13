package com.mab.tasktracker.server;

import com.mab.tasktracker.service.TaskService;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

public class TaskHttpServer {

    private final TaskService taskService;
    private final HttpServer server;
    public TaskHttpServer(TaskService taskService, int port) throws IOException {
        this.taskService = taskService;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.createContext("/tasks", new TaskHandler(taskService));
    }

    public void start() {
        server.start();
        System.out.println("Server started on port " + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped.");
    }
}