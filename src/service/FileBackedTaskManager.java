package service;

import exception.ManagerSaveException;
import exception.ValidationException;
import model.*;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : super.getAllTasks()) {
                writer.write(task.toStringTask());
                writer.write("\n");
            }
            for (Epic epic : super.getAllEpics()) {
                writer.write(epic.toStringTask());
                writer.write("\n");
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                writer.write(subtask.toStringTask());
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки", e);
        }
    }

    public Task fromString(String value) {
        //id,type,name,status,description,epic
        // 0,   1,   2,     3,          4,   5
        String[] split = value.split(",");
        Status status;
        try {
            if (split[3].equals("NEW")) {
                status = Status.NEW;
            } else if (split[3].equals("IN_PROGRESS")) {
                status = Status.IN_PROGRESS;
            } else if (split[3].equals("DONE")) {
                status = Status.DONE;
            } else {
                throw new ValidationException("Не известный тип задачи");
            }

            if (split[1].equals("TASK")) {
                return new Task(split[2], split[4], Integer.parseInt(split[0]), status);
            } else if (split[1].equals("EPIC")) {
                return new Epic(split[2], split[4], Integer.parseInt(split[0]), status);
            } else if (split[1].equals("SUBTASK")) {
                Epic epic = (Epic)getTaskById(Integer.parseInt(split[5]));
                if (epic.getTypeTask() != TypeTask.EPIC) throw new ValidationException("Ошибка привязки Subtask к Epic");
                if (epic.getTypeTask() == null) throw new ValidationException("Ошибка привязки Subtask к Epic");
                return new Subtask(split[2], split[4], Integer.parseInt(split[0]), epic, status);
            }
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    // Восстановление из в файла
    public void loadFromFile() {
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine(); //Пропускаем первую строку
            Task task;
            String line;
            while ((line = reader.readLine()) != null) {
                task = fromString(line);
                if (task.getTypeTask() == TypeTask.TASK) {
                    this.allTask.put(task.getIdTask(), task);
                } else if (task.getTypeTask() == TypeTask.EPIC) {
                    Epic newTask = (Epic) task;
                    this.allEpic.put(task.getIdTask(), newTask);
                } else if (task.getTypeTask() == TypeTask.SUBTASK) {
                    Subtask newTask = (Subtask)task;
                    this.allSubtask.put(task.getIdTask(), newTask);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки", e);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

}
