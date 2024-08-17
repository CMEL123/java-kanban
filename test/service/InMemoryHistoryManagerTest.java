package service;

import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryHistoryManagerTest {
    static InMemoryTaskManager manager = new InMemoryTaskManager();
    static InMemoryHistoryManager historyManager = manager.getHistoryManager();
    static Task t1 = new Task("Задача1", "Задача1d", 1234);
    static Task t2 = new Task("Задача2", "Задача2d", 12345, Status.IN_PROGRESS);
    static Epic e1 = new Epic("Эпик1", "Задача1d", 1424);

    @BeforeAll
    static void beforeAll() {
        manager.addTask(t1);
        manager.addTask(t2);
        manager.addTask(e1);
    }

    //убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void testGetAndAddHistory() {
        Task lastTask;
        //Тест на добавление в историю при просмотре задачи
        manager.getTaskById(t1.getIdTask());
        lastTask = historyManager.getHistory().getLast();
        assertEquals(historyManager.getHistory().size(), 1);
        assertEquals(t1, lastTask);

        manager.getTaskById(t2.getIdTask());
        lastTask = historyManager.getHistory().getLast();
        assertEquals(historyManager.getHistory().size(), 2);
        assertEquals(t2, lastTask);

        manager.getTaskById(t2.getIdTask());
        lastTask = historyManager.getHistory().getLast();
        assertEquals(historyManager.getHistory().size(), 3);
        assertEquals(t2, lastTask);

        manager.getTaskById(t1.getIdTask()); //4
        assertEquals(historyManager.getHistory().size(), 4);
        manager.getTaskById(t1.getIdTask()); //5
        assertEquals(historyManager.getHistory().size(), 5);
        manager.getTaskById(t1.getIdTask()); //6
        assertEquals(historyManager.getHistory().size(), 6);
        manager.getTaskById(e1.getIdTask()); //7
        assertEquals(historyManager.getHistory().size(), 7);
        manager.getTaskById(t1.getIdTask()); //8
        assertEquals(historyManager.getHistory().size(), 8);
        manager.getTaskById(t1.getIdTask()); //9
        assertEquals(historyManager.getHistory().size(), 9);
        manager.getTaskById(e1.getIdTask()); //10
        lastTask = historyManager.getHistory().getLast();
        assertEquals(historyManager.getHistory().size(), 10);
        assertEquals(e1, lastTask);

        //Тест на переполнение (В истории должно хранится 10 последних задач)
        manager.getTaskById(t2.getIdTask());
        lastTask = historyManager.getHistory().getLast();
        assertEquals(historyManager.getHistory().size(), 10);
        assertEquals(t2, lastTask);
        assertNotEquals(t1, historyManager.getHistory().getFirst());
        assertEquals(t2, historyManager.getHistory().getFirst());
;
    }

}