package model;

public class Subtask extends Task {
    private Integer epicId; //эпик, к которому относится подзадача

    public Subtask(String name, String description, int idTask, Epic epic) {
        super(name, description, idTask);
        this.setStatus(Status.NEW);
        this.setEpicId(epic.getIdTask());
        epic.addSubTask(this);
        this.setTypeTask(TypeTask.SUBTASK);
    }

    public Subtask(String name, String description, int idTask, Epic epic, Status status) {
        super(name, description, idTask);
        this.setStatus(status);
        this.setEpicId(epic.getIdTask());
        epic.addSubTask(this);
        this.setTypeTask(TypeTask.SUBTASK);
    }


    @Override
    public String toStringTask() {
        return  super.toStringTask() + this.getEpicId() + ",";
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
