package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    static InMemoryTaskManager manager;
    static Task t1;
    static Task t2;
    static Epic e1;
    static Epic e2;
    static Subtask s1;
    static Subtask s2;
    static Subtask s3;

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
        t1 = new Task("Задача1", "Задача1d", 1234);
        t2 = new Task("Задача2", "Задача2d", 12345, Status.IN_PROGRESS);
        e1 = new Epic("Эпик1", "Задача1d", 1424);
        e2 = new Epic("Эпик2", "Задача2d", 4343);
        s1 = new Subtask("Подзадача1", "Задача1d", 1424, e1, Status.IN_PROGRESS);
        s2 = new Subtask("Подзадача2", "Задача2d", 4343, e1, Status.NEW);
        s3 = new Subtask("Подзадача3", "Задача1d", 1424, e2, Status.DONE);

        manager.addTask(t1);
        manager.addTask(t2);

        manager.addTask(e1);

        s1.setEpicId(e1.getIdTask());
        s2.setEpicId(e1.getIdTask());
        manager.addTask(s1);
        manager.addTask(s2);

        manager.addTask(e2);
        s3.setEpicId(e2.getIdTask());
        manager.addTask(s3);
    }
    @AfterEach
    void afterEach() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
    }

    @Test
    //проверьте, что InMemoryTaskManager действительно
    //добавляет задачи разного типа и может найти их по id;
    void addTask() {
        assertEquals(t1, manager.getTaskById(t1.getIdTask()));
        assertEquals(t2, manager.getTaskById(t2.getIdTask()));
        assertEquals(e1, manager.getTaskById(e1.getIdTask()));
        assertEquals(s1, manager.getTaskById(s1.getIdTask()));
        assertEquals(s3, manager.getTaskById(s3.getIdTask()));
        assertEquals(e2, manager.getTaskById(e2.getIdTask()));
        assertEquals(s2, manager.getTaskById(s2.getIdTask()));
    }

    //Получение списка всех задач
    @Test
    void getAllTasks() {
        assertEquals(manager.getAllTasks().size(), 2);
    }

    //Получение списка всех эпиков
    @Test
    void getAllEpics() {
        assertEquals(manager.getAllEpics().size(), 2);
    }

    //Получение списка всех эпиков
    @Test
    void getAllSubtasks() {
        assertEquals(manager.getAllSubtasks().size(), 3);
    }

    //получать задачи по идентификатору
    @Test
    void getTaskById() {
        assertEquals(t1, manager.getTaskById(t1.getIdTask()));
        assertEquals(t1, manager.getHistory().getLast());
        assertEquals(e1, manager.getTaskById(e1.getIdTask()));
        assertEquals(e1, manager.getHistory().getLast());
        assertEquals(s2, manager.getTaskById(s2.getIdTask()));
        assertEquals(s2, manager.getHistory().getLast());
        assertNull(manager.getTaskById(-1));
        assertEquals(s2, manager.getHistory().getLast());

    }

    //Обновление.
    @Test
    void updateTask() {
        Task t3 = new Task("Задача3", "Задача1d", t2.getIdTask());
        manager.updateTask(t3);
        assertEquals(t3, manager.getTaskById(t3.getIdTask()));
        assertNotEquals(t2, manager.getTaskById(t2.getIdTask()));
        assertNotEquals(t2, t3);
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    void cheakId() {
        Task t4 = new Task("Задача4", "Задача4", t1.getIdTask());
        assertNotEquals(t4, manager.getTaskById(t1.getIdTask()));
    }

    //Удаление по идентификатору.
    @Test
    void deleteTaskById() {
        //До удаления
        assertTrue(e2.getSubtasks().contains(s3.getIdTask()));
        assertEquals(e2.getStatus(), Status.DONE);

        manager.deleteTaskById(s3.getIdTask());
        assertNull(manager.getTaskById(s3.getIdTask()));
        //Проверка у эпика удалилась подзадача
        assertFalse(e2.getSubtasks().contains(s3.getIdTask()));
        //Проверка, что у эпика понялся статус с DONE на NEW
        assertEquals(e2.getStatus(), Status.NEW);
    }

    //Получение списка всех подзадач определённого эпика.
    @Test
    void getSubtaskByEpic() {
        ArrayList<Integer> ids =  e1.getSubtasks();
        ArrayList<Subtask> subtaskByEpic = manager.getSubtaskByEpic(e1.getIdTask());
        for (Subtask subtask : subtaskByEpic) {
            assertTrue(ids.contains(subtask.getIdTask()));
        }
        assertEquals(ids.size(), subtaskByEpic.size());
    }

    @Test
    void deleteAllSubtasks() {
        manager.deleteAllSubtasks();
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    void deleteAllEpics() {
        manager.deleteAllEpics();
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    void deleteAllTasks() {
        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    //Проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    //Проверьте, что объект Subtask нельзя сделать своим же эпиком;
    // Этих проверок не будет
    // (При обсуждении с наставниками мы сошлись на том, что такой тест написать невозможно, так как методы по созданию задач/подзадач/эпиков принимают объекты определенного типа. )
}