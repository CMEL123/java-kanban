package service;

import model.Epic;
import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager  implements HistoryManager {
    public ArrayList<Task> history = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (history.size() == 10 ) history.removeFirst();
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
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
