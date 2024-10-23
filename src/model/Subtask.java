package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId; //эпик, к которому относится подзадача

    public Subtask(String name, String description, int idTask, Epic epic, Duration duration, LocalDateTime startTime) {
        super(name, description, idTask, duration, startTime);
        this.setStatus(Status.NEW);
        this.setEpicId(epic.getIdTask());
        epic.addSubTask(this);
        this.setTypeTask(TypeTask.SUBTASK);
    }

    public Subtask(String name, String description, Epic epic, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.setStatus(Status.NEW);
        this.setEpicId(epic.getIdTask());
        epic.addSubTask(this);
        this.setTypeTask(TypeTask.SUBTASK);
    }

    public Subtask(String name, String description, int idTask, Epic epic, Duration duration, LocalDateTime startTime, Status status) {
        super(name, description, idTask, duration, startTime);
        this.setStatus(status);
        this.setEpicId(epic.getIdTask());
        epic.addSubTask(this);
        this.setTypeTask(TypeTask.SUBTASK);
    }

    public Subtask(String name, String description, Epic epic, Duration duration, LocalDateTime startTime, Status status) {
        super(name, description, duration, startTime);
        this.setStatus(status);
        this.setEpicId(epic.getIdTask());
        epic.addSubTask(this);
        this.setTypeTask(TypeTask.SUBTASK);
    }

    public Subtask(String name, String description, int idTask, int epicId, Duration duration, LocalDateTime startTime, Status status) {
        super(name, description, idTask, duration, startTime);
        this.setStatus(status);
        this.setEpicId(epicId);
        this.setTypeTask(TypeTask.SUBTASK);
    }

    public Subtask(String name, String description, int epicId, Duration duration, LocalDateTime startTime, Status status) {
        super(name, description, duration, startTime, status);
        this.setEpicId(epicId);
        this.setTypeTask(TypeTask.SUBTASK);
    }


    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String retStr =  super.toString();
        retStr += " epicId: " + epicId;
        return retStr;
    }
}
