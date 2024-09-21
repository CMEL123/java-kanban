package model;

import java.util.Objects;

public class Task {
    private String name;        // Название задачи
    private String description; // Описание задачи
    private int    idTask;      // Уникальный номер задачи
    private Status status;      // Статус задачи
    private TypeTask typeTask;        // имя класса

    public Task(String name, String description, int idTask) {
        this.name = name;
        this.description = description;
        this.idTask = idTask;
        this.status = Status.NEW;
        this.typeTask = TypeTask.TASK;
    }

    public Task(String name, String description, int idTask, Status status) {
        this.name = name;
        this.description = description;
        this.idTask = idTask;
        this.status = status;
        this.typeTask = TypeTask.TASK;
    }

    public void setStatus(Status status) {
        this.status = status;
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

}
