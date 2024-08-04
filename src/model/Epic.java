package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer>subtasks; //список подзадач

    public Epic(String name, String description, int idTask) {
        super(name, description, idTask);
        this.subtasks = new ArrayList<Integer>();
        this.setTypeTask(TypeTask.EPIC);
    }

    public void addSubTask( Subtask subtask ){
        if (!subtasks.contains(subtask.getIdTask())) {
            subtasks.add(subtask.getIdTask());
        }
    }

    public void delSubTask(Subtask subtask ){
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
        for (Integer subtask : this.getSubtasks()){
            retStr += subtask + "\n";
        }
        return retStr;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

}
