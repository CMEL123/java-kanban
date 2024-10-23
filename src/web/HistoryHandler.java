package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager managers) {
        super(managers);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        int code = 404;

        // извлеките метод из запроса
        String method = httpExchange.getRequestMethod();
        String[] arrayPath = httpExchange.getRequestURI().getPath().split("/");

        switch (method) {
            case "GET":
                if (arrayPath.length == 2) {
                    code     = 200;
                    response = gson.toJson(manager.getHistory());
                }
                break;
            default:
                response = "Некорректный метод!";
        }

        send(httpExchange, code, response);
    }
}