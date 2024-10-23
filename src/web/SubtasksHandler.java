package web;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    public SubtasksHandler(TaskManager managers) {
        super(managers);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        int code = 404;

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
                            response = "Задача удалена";
                        }
                    }
                    break;
                default:
                    response = "Некорректный метод!";
            }

            send(httpExchange, code, response);

        } catch (JsonSyntaxException exp) {
            exp.printStackTrace();
            System.out.println(exp.getMessage());
            sendException(httpExchange, exp.getMessage());
        }
    }
}
