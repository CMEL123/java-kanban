package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks; //список подзадач

    private LocalDateTime endTime; //Время дата и время завершения задачи

    public Epic(String name, String description, int idTask, Duration duration, LocalDateTime startTime) {
        super(name, description, idTask, duration, startTime);
        this.subtasks = new ArrayList<Integer>();
        this.setTypeTask(TypeTask.EPIC);
        this.endTime  = startTime.plus(duration);
    }

    public Epic(String name, String description, int idTask, Duration duration, LocalDateTime startTime, Status status) {
        super(name, description, idTask, duration, startTime, status);
        this.subtasks = new ArrayList<Integer>();
        this.setTypeTask(TypeTask.EPIC);
        this.endTime  = startTime.plus(duration);
    }

    public Epic(String name, String description, int idTask, Duration duration, LocalDateTime startTime, LocalDateTime endTime, Status status) {
        super(name, description, idTask, duration, startTime, status);
        this.subtasks = new ArrayList<Integer>();
        this.setTypeTask(TypeTask.EPIC);
        this.endTime  = endTime;
    }

    public void addSubTask(Subtask subtask) {
        if (!subtasks.contains(subtask.getIdTask())) {
            subtasks.add(subtask.getIdTask());
        }
    }

    public void delSubTask(Subtask subtask) {
        if (subtasks.contains(subtask.getIdTask())) {
            subtasks.remove((Integer) subtask.getIdTask());
        }
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        String retStr =  super.toString();
        if (this.getSubtasks().isEmpty()) return retStr;
        retStr += " Subtasks:" + "\n";
        for (Integer subtask : this.getSubtasks()) {
            retStr += subtask + "\n";
        }
        return retStr;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void clearSubtasks() {
        this.subtasks.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
