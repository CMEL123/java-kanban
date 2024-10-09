package converter;

import exception.ValidationException;
import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskConverter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");


    public static String toStringTask(Task task) {
        //id,type,name,status,description,duration,startTime,endTime,epic
        String ret = task.getIdTask() + "," +
                task.getTypeTask() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getDuration().toMinutes() + "," +
                task.getStartTime().format(DATE_TIME_FORMATTER) + ",";

        if (task.getTypeTask() == TypeTask.EPIC) ret += task.getEndTime().format(DATE_TIME_FORMATTER) + ",";
        if (task.getTypeTask() == TypeTask.SUBTASK) ret += "," + ((Subtask)task).getEpicId() + ",";

        return ret;
    }

    public static Task fromString(String value) {
        //id,type,name,status,description,duration,startTime,endTime,epic
        // 0,   1,   2,     3,          4,       5,        6,      7,   8
        String[] split = value.split(",");
        Status status = Status.valueOf(split[3]);
        try {
            if (split[1].equals("TASK")) {
                //name, description, idTask, duration, startTime, status
                return new Task(split[2], split[4], Integer.parseInt(split[0]), Duration.ofMinutes(Integer.parseInt(split[5])), LocalDateTime.parse(split[6], DATE_TIME_FORMATTER), status);
            } else if (split[1].equals("EPIC")) {
                //name, description, idTask, duration, startTime, status, endTime
                return new Epic(split[2], split[4], Integer.parseInt(split[0]),  Duration.ofMinutes(Integer.parseInt(split[5])), LocalDateTime.parse(split[6], DATE_TIME_FORMATTER), LocalDateTime.parse(split[7], DATE_TIME_FORMATTER), status);
            } else if (split[1].equals("SUBTASK")) {
                //name, description, idTask, epicId, duration, startTime, status
                return new Subtask(split[2], split[4], Integer.parseInt(split[0]), Integer.parseInt(split[8]), Duration.ofMinutes(Integer.parseInt(split[5])), LocalDateTime.parse(split[6], DATE_TIME_FORMATTER), status);
            } else {
                throw new ValidationException("Не известный тип задачи");
            }
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
