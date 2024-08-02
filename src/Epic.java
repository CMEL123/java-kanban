import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> subtasks; //список подзадач

    public Epic(String name, String description, int idTask) {
        super(name, description, idTask);
        this.subtasks = new ArrayList<Subtask>();
    }

    public void addSubTask( Subtask subtask ){
        if (!subtasks.contains(subtask)) {
            subtasks.add(subtask);
            setStatus(status);
        }
    }

    public void delSubTask( Subtask subtask ){
        if (subtasks.contains(subtask)) {
            subtasks.remove(subtask);
            setStatus(status);
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void setStatus(Status status) {
        if (subtasks.isEmpty()){
           this.status = Status.NEW;
           return ;
        }
        Status new_status = Status.NEW;
        for (Subtask subtask : subtasks) {
            if (subtask.status != Status.NEW){
                new_status = Status.IN_PROGRESS;
                break;
            }
        }
        if (new_status == Status.NEW) {
            this.status = Status.NEW;
            return ;
        }

        for (Subtask subtask : subtasks) {
            if (subtask.status != Status.DONE){
                this.status = Status.IN_PROGRESS;
                return ;
            }
        }
        this.status = Status.DONE;
    }

    @Override
    public String toString() {
        String retStr =  super.toString();
        if (this.getSubtasks().isEmpty()) return retStr;
        retStr += " Subtasks:" + "\n";
        for (Subtask subtask : this.getSubtasks()){
            retStr += "    " + subtask.toString() + "\n";
        }
        return retStr;
    }
}
