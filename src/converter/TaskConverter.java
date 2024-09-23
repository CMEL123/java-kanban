package converter;

import exception.ValidationException;
import model.*;

public class TaskConverter {
    public static String toStringTask(Task task) {
        String ret = task.getIdTask() + "," +
                task.getTypeTask() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + ",";

        if ( task.getTypeTask() == TypeTask.SUBTASK) ret += ((Subtask)task).getEpicId() + ",";

        return ret;
    }

    public static Task fromString(String value) {
        //id,type,name,status,description,epic
        // 0,   1,   2,     3,          4,   5
        String[] split = value.split(",");
        Status status = Status.valueOf(split[3]);
        try {
            if (split[1].equals("TASK")) {
                return new Task(split[2], split[4], Integer.parseInt(split[0]), status);
            } else if (split[1].equals("EPIC")) {
                return new Epic(split[2], split[4], Integer.parseInt(split[0]), status);
            } else if (split[1].equals("SUBTASK")) {
                return new Subtask(split[2], split[4], Integer.parseInt(split[0]), Integer.parseInt(split[5]), status);
            } else {
                throw new ValidationException("Не известный тип задачи");
            }
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
