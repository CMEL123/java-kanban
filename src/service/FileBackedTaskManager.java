package service;

import exception.ManagerSaveException;
import exception.ValidationException;
import converter.TaskConverter;
import model.*;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    public static final String NAME_FILE_CSV = "tasks.csv";

    public FileBackedTaskManager() {
        super();
        this.file = new File(NAME_FILE_CSV);
    }

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    protected void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : super.getAllTasks()) {
                writer.write(TaskConverter.toStringTask(task));
                writer.write("\n");
            }
            for (Epic epic : super.getAllEpics()) {
                writer.write(TaskConverter.toStringTask(epic));
                writer.write("\n");
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                writer.write(TaskConverter.toStringTask(subtask));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки", e);
        }
    }

   // Восстановление из в файла
   public static FileBackedTaskManager loadFromFile(File file) {
       FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine(); //Пропускаем первую строку
            String line;
            int max_id = 0;
            while ((line = reader.readLine()) != null) {
                Task task = TaskConverter.fromString(line);
                if (task == null) {
                    throw new ValidationException("Ошибка загрузки");
                } else if (task.getTypeTask() == TypeTask.TASK) {
                    manager.allTask.put(task.getIdTask(), task);
                } else if (task.getTypeTask() == TypeTask.EPIC) {
                    Epic newTask = (Epic) task;
                    manager.allEpic.put(task.getIdTask(), newTask);
                } else if (task.getTypeTask() == TypeTask.SUBTASK) {
                    Subtask newTask = (Subtask)task;
                    Epic epic = (Epic)manager.getTaskById(newTask.getEpicId());
                    if (epic.getTypeTask() != TypeTask.EPIC) throw new ValidationException("Ошибка привязки Subtask к Epic");
                    if (epic.getTypeTask() == null) throw new ValidationException("Ошибка привязки Subtask к Epic");
                    epic.addSubTask(newTask);
                    manager.allSubtask.put(newTask.getIdTask(), newTask);
                }
                if (max_id < task.getIdTask()) max_id = task.getIdTask();
            }
            manager.identifier = max_id;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки", e);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return manager;
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
