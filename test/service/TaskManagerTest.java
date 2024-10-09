package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    static Task t1;
    static Task t2;
    static Epic e1;
    static Epic e2;
    static Subtask s1;
    static Subtask s2;
    static Subtask s3;

    protected abstract T createTaskManager() throws IOException;

    @BeforeEach
    void beforeEach() throws IOException {
        manager = createTaskManager();
        LocalDateTime nowTime = LocalDateTime.now();
        t1 = new Task("Задача1", "Задача1d", 1234, Duration.ofMinutes(100), nowTime);
        nowTime = nowTime.plusMinutes(150);
        t2 = new Task("Задача2", "Задача2d", 12345, Duration.ofMinutes(100), nowTime, Status.IN_PROGRESS);
        nowTime = nowTime.plusMinutes(150);
        e1 = new Epic("Эпик1", "Задача1d", 1424, Duration.ofMinutes(100), nowTime);
        nowTime = nowTime.plusMinutes(150);
        e2 = new Epic("Эпик2", "Задача2d", 4343, Duration.ofMinutes(100), nowTime);
        nowTime = nowTime.plusMinutes(150);
        s1 = new Subtask("Подзадача1", "Задача1d", 1424, e1, Duration.ofMinutes(100), nowTime, Status.IN_PROGRESS);
        nowTime = nowTime.plusMinutes(150);
        s2 = new Subtask("Подзадача2", "Задача2d", 4343, e1, Duration.ofMinutes(100), nowTime, Status.NEW);
        nowTime = nowTime.plusMinutes(150);
        s3 = new Subtask("Подзадача3", "Задача1d", 1424, e2, Duration.ofMinutes(100), nowTime, Status.DONE);

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
    void addTask()  {
        assertEquals(t1, manager.getTaskById(t1.getIdTask()));
        assertEquals(t2, manager.getTaskById(t2.getIdTask()));
        assertEquals(e1, manager.getTaskById(e1.getIdTask()));
        assertEquals(s1, manager.getTaskById(s1.getIdTask()));
        assertEquals(s3, manager.getTaskById(s3.getIdTask()));
        assertEquals(e2, manager.getTaskById(e2.getIdTask()));
        assertEquals(s2, manager.getTaskById(s2.getIdTask()));

        //Тест, что нельзя добавить с пересечением по времени
        Task t3 = new Task("Задача1", "Задача1d", 1234, Duration.ofMinutes(100), t1.getStartTime()); //с одинаковым StartTime
        manager.addTask(t3);
        assertFalse(manager.getAllTasks().contains(t3));
        Task t4 = new Task("Задача1", "Задача1d", 1234, Duration.ofMinutes(100), t1.getStartTime().plusMinutes(1)); //с одинаковым StartTime
        manager.addTask(t4);
        assertFalse(manager.getAllTasks().contains(t4));
        Task t5 = new Task("Задача1", "Задача1d", 1234, Duration.ofMinutes(100), t1.getStartTime().plusMinutes(-1)); //с одинаковым StartTime
        manager.addTask(t5);
        assertFalse(manager.getAllTasks().contains(t5));
        Task t6 = new Task("Задача1", "Задача1d", 1234, Duration.ofMinutes(10000), t1.getStartTime().plusMinutes(-1)); //с одинаковым StartTime
        manager.addTask(t6);
        assertFalse(manager.getAllTasks().contains(t5));

        //Проверка изменения атрибутов endTime StartTime Duration у класса Epic
        long oldDurationMinutes = e1.getDuration().toMinutes();
        Subtask s4 = new Subtask("Подзадача1", "Задача1d", 14247, e1.getIdTask(), Duration.ofMinutes(100), e1.getStartTime().plusMinutes(-2000), Status.IN_PROGRESS);

        manager.addTask(s4);
        assertEquals(e1.getStartTime(), s4.getStartTime());
        assertNotEquals(e1.getDuration().toMinutes(), oldDurationMinutes);
        assertEquals(e1.getDuration().toMinutes(), oldDurationMinutes + 100);

        Subtask s5 = new Subtask("Подзадача1", "Задача1d", 14247, e1.getIdTask(), Duration.ofMinutes(100), e1.getStartTime().plusMinutes(3000), Status.IN_PROGRESS);

        manager.addTask(s5);
        assertEquals(e1.getEndTime(), s5.getEndTime());
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
        Task t3 = new Task("Задача3", "Задача1d", t2.getIdTask(), t2.getDuration(), t2.getStartTime());
        manager.updateTask(t3);
        assertEquals(t3, manager.getTaskById(t3.getIdTask()));
        assertNotEquals(t2, manager.getTaskById(t2.getIdTask()));
        assertNotEquals(t2, t3);


    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    void cheakId() {
        Task t4 = new Task("Задача4", "Задача4", t1.getIdTask(), t1.getDuration(), t1.getStartTime());
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
