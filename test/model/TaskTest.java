package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testEquals() {
        Task t1 = new Task("Задача1", "Задача1d", 1234);  //Проверяемый объект
        Task t2 = new Task("Задача1", "Задача1d", 1234);  //Ничего не менял
        Task t3 = new Task("Задача1", "Задача1d", 1234);  //Описание
        Task t4 = new Task("Задача2", "Задача1d", 1234);  //Изменил имя
        Task t5 = new Task("Задача1", "Задача1d", 12345); //Изменил id
        Epic e1 = new Epic("Задача1", "Задача1d", 1234);  //Изменил класс, на подкласс

        assertTrue(t1.equals(t2));
        assertTrue(t1.equals(t3));
        assertFalse(t1.equals(t4));
        assertFalse(t1.equals(t4));
        assertFalse(t1.equals(e1));
    }
}