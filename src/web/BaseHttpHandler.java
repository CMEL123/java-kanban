package web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import converter.JsonTaskParser;
import model.*;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BaseHttpHandler implements HttpHandler {
    TaskManager manager;

    public BaseHttpHandler(TaskManager man) {
        manager = man;
    }

    protected void sendText(HttpExchange h, Integer code, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        int code = 404;

        Gson gson = JsonTaskParser.gson;

        try {
            // извлеките метод из запроса
            String method = httpExchange.getRequestMethod();
            String[] arrayPath = httpExchange.getRequestURI().getPath().split("/");
            int idTask = -1;
            if (arrayPath.length > 2) idTask = Integer.parseInt(arrayPath[2]);

            switch (method) {
                case "GET":
                    if (arrayPath.length == 2) {
                        code = 200;
                        response = gson.toJson(manager.getAllTasks());
                    } else if (arrayPath.length == 3) {
                        Task newTask = manager.getTaskById(idTask);
                        if (newTask != null) {
                            code = 200;
                            response = gson.toJson(newTask);;
                        }
                    } else if (arrayPath.length == 4 && arrayPath[1].equals("epics") && arrayPath[3].equals("subtasks")) {
                        ArrayList<Subtask> arrayListSubtask = manager.getSubtaskByEpic(idTask);
                        if (arrayListSubtask != null) {
                            code = 200;
                            response = gson.toJson(arrayListSubtask);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String subtitlesJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = getTaskFromJson(subtitlesJson, arrayPath[1], gson);

                    if (task.getIdTask() == 0) {
                        Task newTask = manager.addTask(task);
                        if (newTask == null) {
                            code = 406;
                            response = "Not Acceptable";
                        } else {
                            code = 201;
                        }
                    } else {
                        Task newTask = manager.updateTask(task);

                        if (newTask == null) {
                            code = 406;
                            response = "Not Acceptable";
                        } else {
                            code = 201;
                        }
                    }
                    break;

                case "DELETE":
                    if (arrayPath.length == 3) {
                        Task newTask = manager.deleteTaskById(idTask);

                        if (newTask == null) {
                            code = 406;
                            response = "Not Acceptable";
                        } else {
                            code = 200;
                            response = "";
                        }
                    }
                    break;
                default:
                    response = "Некорректный метод!";
            }
            sendText(httpExchange, code, response);

        } catch (JsonSyntaxException exp) {
            exp.printStackTrace();
            System.out.println(exp.getMessage());
            sendText(httpExchange, code, response);
        }
    }

    private Task getTaskFromJson(String subtitlesJson, String typeTask, Gson gson) {
        if (typeTask.equals("tasks")) {
            return gson.fromJson(subtitlesJson, new TaskTypeToken().getType());
        } else if (typeTask.equals("epics")) {
            return gson.fromJson(subtitlesJson, new EpicTypeToken().getType());
        } else if (typeTask.equals("subtasks")) {
            return gson.fromJson(subtitlesJson, new SubtaskTypeToken().getType());
        } else {
            return null;
        }
    }

    // вспомогательный класс для определения типа коллекции и типа её элементов
    static class TaskTypeToken extends TypeToken<Task> {
        // здесь ничего не нужно реализовывать
    }

    // вспомогательный класс для определения типа коллекции и типа её элементов
    static class EpicTypeToken extends TypeToken<Epic> {
        // здесь ничего не нужно реализовывать
    }

    // вспомогательный класс для определения типа коллекции и типа её элементов
    static class SubtaskTypeToken extends TypeToken<Subtask> {
        // здесь ничего не нужно реализовывать
    }

}