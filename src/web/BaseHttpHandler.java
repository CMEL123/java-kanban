package web;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.*;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    TaskManager manager;
    Gson gson;

    public BaseHttpHandler(TaskManager man) {
        manager = man;
        gson = HttpTaskServer.getGson();
    }

    protected void sendText(HttpExchange exchange) throws IOException {
        sendText(exchange, "");
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        if (resp.length == 0 ) {
            exchange.sendResponseHeaders(201, 0);
        } else {
            exchange.sendResponseHeaders(200, resp.length);
        }
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String text = "Объект не был найден.";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String text = "Задача пересекается с уже существующими.";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(406, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendException(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(500, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public Task getTaskFromJson(String subtitlesJson, String typeTask, Gson gson) {
        if (typeTask.equals("tasks")) {
            return gson.fromJson(subtitlesJson, Task.class);
        } else if (typeTask.equals("epics")) {
            return gson.fromJson(subtitlesJson, Epic.class);
        } else if (typeTask.equals("subtasks")) {
            return gson.fromJson(subtitlesJson, Subtask.class);
        } else {
            return null;
        }
    }

    protected void send(HttpExchange exchange, int code, String text) throws IOException {
        if (code == 200){
            sendText(exchange, text);
        } else if (code == 201){
            sendText(exchange);
        } else if (code == 406){
            sendHasInteractions(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

}