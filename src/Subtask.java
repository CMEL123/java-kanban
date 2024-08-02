
public class Subtask extends Task{
    Epic epic; //эпик, к которому относится подзадача

    public Subtask(String name, String description, int idTask, Epic epic) {
        super(name, description, idTask);
        this.status = Status.NEW;
        this.epic = epic;
        this.epic.addSubTask(this);
    }

    public Subtask(String name, String description, int idTask, Epic epic, Status status) {
        super(name, description, idTask);
        this.status = status;
        this.epic = epic;
        this.epic.addSubTask(this);
    }
}
