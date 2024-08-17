package service;

import model.*;

import java.util.ArrayList;

public interface TaskManager {

    //Создание. Сам объект должен передаваться в качестве параметра.
    public abstract Task addTask(Task task);

    //получать задачи по типу
    public abstract ArrayList<Task> getAllTasks();
    public abstract ArrayList<Epic> getAllEpics();
    public abstract ArrayList<Subtask> getAllSubtasks();

    //Удаление всех задач
    public abstract void deleteAllTasks();

    public abstract void deleteAllEpics();

    public abstract void deleteAllSubtasks();

    //получать задачи по идентификатору
     public abstract Task getTaskById(int id);

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public abstract void updateTask(Task task);

    //Удаление по идентификатору.
    public abstract void deleteTaskById(int id);

    //Получение списка всех подзадач определённого эпика.
    public abstract ArrayList<Subtask> getSubtaskByEpic(int epicID);

    //История просмотров задач
    //он должен возвращать последние 10 просмотренных задач
    public abstract ArrayList<Task> getHistory();

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtaskByEpic(epic.getIdTask())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
