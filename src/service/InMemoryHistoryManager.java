package service;

import model.Epic;
import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager  implements HistoryManager {
    private static final int MAX_HISTORY_VALUE = 10;
    public ArrayList<Task> history = new ArrayList<>(MAX_HISTORY_VALUE);

    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY_VALUE ) history.removeFirst();
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public String toString() {
        String retStr = "";
        for (int i = 0; i < this.history.size(); i++) {
            retStr += history.get(i).toString();
        }
        return retStr;
    }
}
