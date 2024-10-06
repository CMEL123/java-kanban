package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void addSubTask() {
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424, Duration.ofMinutes(100), LocalDateTime.now());
        //При инициализации вызывается метод addSubTask
        Subtask s1 = new Subtask("Подзадача1", "Задача1d", 1424, e1, Duration.ofMinutes(100), LocalDateTime.now(), Status.IN_PROGRESS);

        assertEquals(e1.getSubtasks().size(), 1);
        assertEquals(s1.getIdTask(), e1.getSubtasks().getLast());

        //При повторном добавлении ничего не добавиться
        e1.addSubTask(s1);

        assertEquals(e1.getSubtasks().size(), 1);
        assertEquals(s1.getIdTask(), e1.getSubtasks().getLast());

    }

    @Test
    void delSubTask() {
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424, Duration.ofMinutes(100), LocalDateTime.now());
        Subtask s1 = new Subtask("Подзадача1", "Задача1d", 1424, e1, Duration.ofMinutes(100), LocalDateTime.now(), Status.IN_PROGRESS);

        assertEquals(e1.getSubtasks().size(), 1);
        assertEquals(s1.getIdTask(), e1.getSubtasks().getLast());


        Subtask s2 = new Subtask("Подзадача2", "Задача1d", 14244, e1, Duration.ofMinutes(100), LocalDateTime.now(), Status.NEW);
        assertEquals(e1.getSubtasks().size(), 2);
        assertEquals(s2.getIdTask(), e1.getSubtasks().getLast());

        e1.delSubTask(s2);
        assertEquals(e1.getSubtasks().size(), 1);
        assertEquals(s1.getIdTask(), e1.getSubtasks().getLast());

        e1.delSubTask(s1);
        assertEquals(e1.getSubtasks().size(), 0);
    }

    @Test
    void clearSubtasks() {
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424, Duration.ofMinutes(100), LocalDateTime.now());
       new Subtask("Подзадача1", "Задача1d", 1424, e1, Duration.ofMinutes(100), LocalDateTime.now(), Status.IN_PROGRESS);
       new Subtask("Подзадача2", "Задача1d", 14244, e1, Duration.ofMinutes(100), LocalDateTime.now(), Status.NEW);

        assertEquals(e1.getSubtasks().size(), 2);
        e1.clearSubtasks();
        assertEquals(e1.getSubtasks().size(), 0);

    }
}