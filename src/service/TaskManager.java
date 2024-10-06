package service;

import exception.ValidationException;
import model.*;

import java.util.ArrayList;

public interface TaskManager {

    //Создание. Сам объект должен передаваться в качестве параметра.
    public void addTask(Task task);

    //получать задачи по типу
    public ArrayList<Task> getAllTasks();

    public ArrayList<Epic> getAllEpics();

    public ArrayList<Subtask> getAllSubtasks();

    //Удаление всех задач
    public void deleteAllTasks();

    public void deleteAllEpics();

    public void deleteAllSubtasks();

    //получать задачи по идентификатору
     public Task getTaskById(int id);

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task);

    //Удаление по идентификатору.
    public void deleteTaskById(int id);

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getSubtaskByEpic(int epicID);

    //История просмотров задач
    //он должен возвращать последние 10 просмотренных задач
    public ArrayList<Task> getHistory();

    //Выводим список задач в порядке приоритета
    public ArrayList<Task> getPrioritizedTasks();
}
