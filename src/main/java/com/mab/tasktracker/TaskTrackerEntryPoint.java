package com.mab.tasktracker;

import com.mab.tasktracker.service.TaskService;
import com.mab.tasktracker.server.TaskHttpServer;

public class TaskTrackerEntryPoint {
    public static void main(String[] args) {
        final TaskHttpServer server;
        TaskService taskService = new TaskService();
        try {
            server = new TaskHttpServer(taskService, 8080);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}