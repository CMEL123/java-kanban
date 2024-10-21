package web;

import com.google.gson.Gson;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() throws IOException {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now(), Status.NEW);
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now(), Status.NEW);
        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(gson.toJson(tasksFromManager), response.body());

        //Проверка на выдачу по id
        URI url2 = URI.create("http://localhost:8080/tasks/" + task.getIdTask());
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
        assertEquals(gson.toJson(task), response2.body());

        //Проверка на выдачу по не сущ. id
        URI url3 = URI.create("http://localhost:8080/tasks/-1");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());
    }

    //Проверка на update
    @Test
    public void updateTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now(), Status.NEW);
        manager.addTask(task);
        task.setName("Test 3");
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals("Test 3", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    //Проверка на удаление
    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now(), Status.NEW);
        manager.addTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/"  + task.getIdTask());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");

    }

    //Проверка на просмотр истории
    @Test
    public void getHistory() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Duration.ofMinutes(5), LocalDateTime.now(), Status.NEW);
        manager.addTask(task);
        Task task2 = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(10), Status.NEW);
        manager.addTask(task2);

        manager.getTaskById(task.getIdTask());
        manager.getTaskById(task2.getIdTask());
        manager.getTaskById(task.getIdTask());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getHistory();
        assertEquals(gson.toJson(tasksFromManager), response.body());

    }

    //Проверка на удаление
    @Test
    public void getPrioritized() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Duration.ofMinutes(5), LocalDateTime.now(), Status.NEW);
        manager.addTask(task);
        Task task2 = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(10), Status.NEW);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getPrioritizedTasks();
        assertEquals(gson.toJson(tasksFromManager), response.body());

    }

    @Test
    public void addSubtask() throws IOException, InterruptedException {
        LocalDateTime nowTime = LocalDateTime.now();
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424, Duration.ofMinutes(100), nowTime);
        nowTime = nowTime.plusMinutes(150);
        manager.addTask(e1);
        Subtask s1 = new Subtask("Подзадача1", "Задача1d", e1.getIdTask(), Duration.ofMinutes(100), nowTime, Status.IN_PROGRESS);

        String taskJson = gson.toJson(s1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");


        URI url2 = URI.create("http://localhost:8080/epics/" + e1.getIdTask() + "/subtasks");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
        assertEquals(gson.toJson(manager.getSubtaskByEpic(e1.getIdTask())), response2.body());
    }
} 