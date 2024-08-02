import java.util.Objects;

public class Task {
    String name;        // Название задачи
    String description; // Описание задачи
    int    idTask;      // Уникальный номер задачи
    Status status;      // Статус задачи

    public Task(String name, String description, int idTask) {
        this.name = name;
        this.description = description;
        this.idTask = idTask;
        this.status = Status.NEW;
    }

    public Task(String name, String description, int idTask, Status status) {
        this.name = name;
        this.description = description;
        this.idTask = idTask;
        this.status = status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(idTask, otherTask.idTask) ;
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
}
