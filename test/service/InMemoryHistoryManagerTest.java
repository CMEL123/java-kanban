package service;

import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryHistoryManagerTest {

    //убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void add() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task t1 = new Task("Задача1", "Задача1d", 1234);
        manager.addTask(t1);

        manager.getTaskById(t1.getIdTask());
        assertEquals(manager.getHistory().size(), 1);
        assertEquals(t1, manager.getHistory().getLast());
    }

    @Test
    void get() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task t1 = new Task("Задача1", "Задача1d", 1234);
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424);

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
        Task t1 = new Task("Задача1", "Задача1d", 1234);
        Task t2 = new Task("Задача2", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t3 = new Task("Задача3", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t4 = new Task("Задача4", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t5 = new Task("Задача5", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t6 = new Task("Задача6", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t7 = new Task("Задача7", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t8 = new Task("Задача8", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t9 = new Task("Задача9", "Задача2d", 12345, Status.IN_PROGRESS);
        Task t10 = new Task("Задача10", "Задача2d", 12345, Status.IN_PROGRESS);
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424);

        manager.addTask(t1);
        manager.addTask(t2);
        manager.addTask(t3);
        manager.addTask(t4);
        manager.addTask(t5);
        manager.addTask(t6);
        manager.addTask(t7);
        manager.addTask(t8);
        manager.addTask(t9);
        manager.addTask(t10);
        manager.addTask(e1);

        //Тест на добавление в историю при просмотре задачи
        manager.getTaskById(t1.getIdTask());
        assertEquals(manager.getHistory().size(), 1);
        manager.getTaskById(t2.getIdTask());
        assertEquals(manager.getHistory().size(), 2);
        manager.getTaskById(t3.getIdTask());
        assertEquals(manager.getHistory().size(), 3);
        manager.getTaskById(t4.getIdTask()); //4
        assertEquals(manager.getHistory().size(), 4);
        manager.getTaskById(t5.getIdTask()); //5
        assertEquals(manager.getHistory().size(), 5);
        manager.getTaskById(t6.getIdTask()); //6
        assertEquals(manager.getHistory().size(), 6);
        manager.getTaskById(t7.getIdTask()); //7
        assertEquals(manager.getHistory().size(), 7);
        manager.getTaskById(t8.getIdTask()); //8
        assertEquals(manager.getHistory().size(), 8);
        manager.getTaskById(t9.getIdTask()); //9
        assertEquals(manager.getHistory().size(), 9);
        manager.getTaskById(t10.getIdTask()); //10
        assertEquals(manager.getHistory().size(), 10);
        assertEquals(t10, manager.getHistory().getLast());

        //11
        manager.getTaskById(e1.getIdTask());
        assertEquals(manager.getHistory().size(), 11);

        //Проверка на то что если вызвать повторно, в истории останется 1 задача
        manager.getTaskById(t2.getIdTask());
        assertEquals(manager.getHistory().size(), 11);
        assertEquals(t2, manager.getHistory().getLast());

    }



}