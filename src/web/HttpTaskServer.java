package web;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import converter.JsonTaskParser;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    public static TaskManager taskManager = Managers.getDefault();
    public static HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static void main(String[] args) throws IOException {
        start(); // запускаем сервер}(
    }

    public static void start() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new BaseHttpHandler(taskManager));
        httpServer.createContext("/epics", new BaseHttpHandler(taskManager));
        httpServer.createContext("/subtasks", new BaseHttpHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
    }

    public static void stop() throws IOException {
        httpServer.stop(2);
    }

    public static Gson getGson() {
        return JsonTaskParser.gson;
    }
}

