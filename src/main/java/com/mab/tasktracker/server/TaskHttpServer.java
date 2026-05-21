package com.mab.tasktracker.server;

import com.mab.tasktracker.service.TaskService;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

public class TaskHttpServer {
    private final HttpServer server;
    private final TaskHandler taskHandler;
    public TaskHttpServer(TaskService taskService, int port) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.taskHandler = new TaskHandler(taskService);
        server.createContext("/", taskHandler);
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