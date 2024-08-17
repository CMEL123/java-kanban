package service;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    public abstract void add(Task task);
    public abstract ArrayList<Task> getHistory();
}
