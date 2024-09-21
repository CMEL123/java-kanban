package service;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    static File tmpFile;
    @Override
    protected FileBackedTaskManager createTaskManager() throws IOException {
        tmpFile = File.createTempFile("task", ".csv");
        return new FileBackedTaskManager(tmpFile);
    }

    //Проверка сохранение нескольких задач
    @Test
    void saveTasks() throws IOException {
        //В beforeEach, они уже должны быть добавлены в файл при вызове метода addTask
        //Проверяем это
        int count = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(manager.getFile(), UTF_8))) {
            while ((reader.readLine()) != null) {
                count++;
            }
        }
        assertEquals(count, (1 + manager.getAllTasks().size() + manager.getAllSubtasks().size() + manager.getAllEpics().size()));
    }

    //Проверка загрузки нескольких задач.
    @Test
    void loadTasks() throws IOException {
        FileBackedTaskManager manager2 = new FileBackedTaskManager(tmpFile);
        manager2.loadFromFile();
        assertEquals(manager.getAllTasks().size(), manager2.getAllTasks().size());
        assertEquals(manager.getAllSubtasks().size(), manager2.getAllSubtasks().size());
        assertEquals(manager.getAllEpics().size(), manager2.getAllEpics().size());
        assertEquals(manager.getTaskById(t1.getIdTask()), manager2.getTaskById(t1.getIdTask()));
        assertEquals(manager.getTaskById(e1.getIdTask()), manager2.getTaskById(e1.getIdTask()));
        assertEquals(manager.getTaskById(s1.getIdTask()), manager2.getTaskById(s1.getIdTask()));
    }

    //Проверка сохранение и загрузку пустого файла
    @Test
    void saveAndLoadEmptyFile() throws IOException {
        File tmpFile2 = File.createTempFile("task", ".csv");
        FileBackedTaskManager manager2 = new FileBackedTaskManager(tmpFile2);
        assertEquals(manager2.getAllTasks().size(), 0);
        assertEquals(manager2.getAllSubtasks().size(), 0);
        assertEquals(manager2.getAllEpics().size(), 0);
        manager2.save();
        manager2.loadFromFile();
        assertEquals(manager2.getAllTasks().size(), 0);
        assertEquals(manager2.getAllSubtasks().size(), 0);
        assertEquals(manager2.getAllEpics().size(), 0);
    }
}
