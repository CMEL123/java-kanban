package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void newSubtask() {
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424);
        //При инициализации вызывается метод addSubTask
        Subtask s1 = new Subtask("Подзадача1", "Задача1d", 1424, e1, Status.IN_PROGRESS);

        assertEquals(s1.getEpicId(), e1.getIdTask());
        assertTrue(e1.getSubtasks().contains(s1.getIdTask()));
    }
}