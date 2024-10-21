package service;

import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryHistoryManagerTest {

    //убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void add() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task t1 = new Task("Задача1", "Задача1d", 1234, Duration.ofMinutes(100), LocalDateTime.now());
        manager.addTask(t1);

        manager.getTaskById(t1.getIdTask());
        assertEquals(manager.getHistory().size(), 1);
        assertEquals(t1, manager.getHistory().getLast());
    }

    @Test
    void get() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task t1 = new Task("Задача1", "Задача1d", 1234, Duration.ofMinutes(100), LocalDateTime.now());
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424, Duration.ofMinutes(100), t1.getEndTime().plusMinutes(1));

        manager.addTask(t1);
        manager.addTask(e1);

        manager.getTaskById(t1.getIdTask());
        assertEquals(manager.getHistory().size(), 1);
        assertEquals(t1, manager.getHistory().getLast());

        manager.getTaskById(e1.getIdTask());
        assertEquals(manager.getHistory().size(), 2);
        assertEquals(e1, manager.getHistory().getLast());

        assertEquals(t1, manager.getHistory().getFirst());
    }

    //Тест на переполнение (В истории может храниться более 10 задач)
    @Test
    void savelastTenTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task t = null;
        //Тест на добавление в историю при просмотре задачи
        for (int i = 1; i < 12; i++) {
            t = new Task("Задача" + i, "Задача" + i + "d", i, Duration.ofMinutes(50), LocalDateTime.now().plusMinutes(100*i), Status.IN_PROGRESS);
            manager.addTask(t);
            manager.getTaskById(t.getIdTask());
            assertEquals(manager.getHistory().size(), i);
        }

        //Проверка на то что если вызвать повторно, в истории останется 1 задача
        manager.getTaskById(t.getIdTask());
        assertEquals(manager.getHistory().size(), 11);
        assertEquals(t, manager.getHistory().getLast());

    }



}