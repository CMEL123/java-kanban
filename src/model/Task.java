package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;        // Название задачи
    private String description; // Описание задачи
    private int    idTask;      // Уникальный номер задачи
    private Status status;      // Статус задачи
    private TypeTask typeTask;        // имя класса
    private Duration duration; //продолжительность задачи в минутах
    private LocalDateTime startTime; //дата и время начала выполнения задачи.

    public Task(String name, String description, int idTask, Duration duration, LocalDateTime startTime) {
        this(name,description,idTask,duration,startTime,Status.NEW);
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this(name,description,duration,startTime,Status.NEW);
    }

    public Task(String name, String description, int idTask, Duration duration, LocalDateTime startTime, Status status) {
        this(name,description,duration,startTime,status);
        this.idTask = idTask;
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(idTask, otherTask.idTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, idTask);
    }

    @Override
    public String toString() {
        return  "Class: " + this.getClass().getSimpleName() + ", " +
                "name: " + name + ", " +
               "description: " + description + ", " +
                "id: " + idTask + ", " +
               "status: " + status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public Status getStatus() {
        return status;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public void setTypeTask(TypeTask typeTask) {
        this.typeTask = typeTask;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
