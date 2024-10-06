package service;

import exception.ValidationException;
import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected int identifier = 0;

    protected final HashMap<Integer, Task> allTask;
    protected final HashMap<Integer, Epic> allEpic;
    protected final HashMap<Integer, Subtask> allSubtask;
    protected final HistoryManager historyManager;
    TreeSet<Task> sortTasksByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager() {
        allTask    =  new HashMap<Integer, Task>();
        allEpic    =  new HashMap<Integer, Epic>();
        allSubtask =  new HashMap<Integer, Subtask>();
        historyManager = new InMemoryHistoryManager();
    }

    private int getIdentifier() {
        return identifier++;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addTask(Task task) {
        try {
            if (sortTasksByStartTime.stream().anyMatch(t -> cheakTaskByStartTime(t,task)))
                throw new ValidationException("Пересечение времени");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return;
        }

        task.setIdTask(getIdentifier());
        if (task.getTypeTask() == TypeTask.TASK) {
            allTask.put(task.getIdTask(), task);
        } else if (task.getTypeTask() == TypeTask.EPIC) {
            Epic newTask = (Epic) task;
            allEpic.put(task.getIdTask(), newTask);
            //Мне сказали новый эпик, всегда будет с пустым списком подзадач
            //Поэтому на всякий
            newTask.setSubtasks(new ArrayList<Integer>());
        } else if (task.getTypeTask() == TypeTask.SUBTASK) {
            Subtask newTask = (Subtask)task;
            //Если в allEpic нет эпика с id, который указан в новой подзадаче,
            // то надо без сохранения завершать метод через return
            if (allEpic.containsKey(newTask.getEpicId())) {
                allSubtask.put(task.getIdTask(), newTask);
                allEpic.get(newTask.getEpicId()).addSubTask(newTask);
                updateEpicStatus(newTask.getEpicId());
                updateEpicTime(newTask.getEpicId());
            }
        }
        sortTasksByStartTime.add(task);
    }

    //получать задачи по типу
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<Task>(allTask.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<Epic>(allEpic.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<Subtask>(allSubtask.values());
    }

    //Удаление всех задач
    @Override
    public void deleteAllTasks() {
        allTask.clear();
    }

    @Override
    public void deleteAllEpics() {
        allSubtask.clear();
        allEpic.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : allEpic.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
        allSubtask.clear();
    }


    //получать задачи по идентификатору
    @Override
    public Task getTaskById(int id) {
        Task retTask;
        if (allTask.containsKey(id)) {
            retTask = allTask.get(id);
        } else if (allEpic.containsKey(id)) {
            retTask = ((Task) allEpic.get(id));
        } else if (allSubtask.containsKey(id)) {
            retTask = ((Task) allSubtask.get(id));
        } else {
            return null;
        }
        historyManager.add(retTask);
        return retTask;
    }


    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        int id = task.getIdTask();
        Task oldTask = getTaskById(id);
        sortTasksByStartTime.remove(oldTask);
        try {
            if (sortTasksByStartTime.stream().anyMatch(t -> cheakTaskByStartTime(t, task))) {
                throw new ValidationException("Пересечение времени");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            sortTasksByStartTime.add(oldTask);
            return;
        }

        if (task.getTypeTask() == TypeTask.TASK) {
            if (allTask.containsKey(id)) {
                allTask.put(id, task);
            }
        } else if (task.getTypeTask() == TypeTask.EPIC) {
            if (allEpic.containsKey(id)) {
                allEpic.put(id, (Epic)task);
            }
        } else if (task.getTypeTask() == TypeTask.SUBTASK) {
            if (allSubtask.containsKey(id)) {
                allSubtask.put(id, (Subtask)task);
                updateEpicStatus(((Subtask) task).getEpicId());
                updateEpicTime(((Subtask) task).getEpicId());
            }
        }
        sortTasksByStartTime.add(task);
    }

    //Удаление по идентификатору.
    @Override
    public void deleteTaskById(int id) {
        Task delTask = getTaskById(id);
        if (delTask == null) {
            return;
        } else if (delTask.getTypeTask() == TypeTask.TASK) {
            allTask.remove(id);
        } else if (delTask.getTypeTask() == TypeTask.EPIC) {
            for (int subtaskId : ((Epic) delTask).getSubtasks()) {
                allSubtask.remove(subtaskId);
            }
            allEpic.remove(id);
        } else if (delTask.getTypeTask() == TypeTask.SUBTASK) {
            Subtask newDelTask = (Subtask)delTask;
            Epic epic = allEpic.get(newDelTask.getEpicId());
            epic.delSubTask(newDelTask);
            allSubtask.remove(id);
            updateEpicStatus(epic.getIdTask());
            updateEpicTime(epic.getIdTask());
        }
        historyManager.remove(id);
        sortTasksByStartTime.remove(delTask);
    }

    //Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Subtask> getSubtaskByEpic(int epicID) {
        Epic epic = allEpic.get(epicID);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (epic != null) {
            for (int subtaskId : epic.getSubtasks()) {
                subtasks.add(allSubtask.get(subtaskId));
            }
        }
        return subtasks;
    }

    @Override
    public String toString() {
        String retStr = "";
        retStr += "Task" + ": " + "\n";
        for (Task task : this.allTask.values()) {
            retStr += task.toString() + "\n";
        }
        retStr += "Epic" + ": " + "\n";
        for (Epic task : this.allEpic.values()) {
            retStr += task.toString() + "\n";
        }
        retStr += "Subtask" + ": " + "\n";
        for (Subtask task : this.allSubtask.values()) {
            retStr += task.toString() + "\n";
        }
        retStr += "History" + ": " + "\n";
        for (Task task : this.getHistory()) {
            retStr += task.toString() + "\n";
        }
        return retStr;
    }

    private void updateEpicStatus(int epicId) {
        Epic editEpic = allEpic.get(epicId);
        if (editEpic.getSubtasks().isEmpty()) {
            editEpic.setStatus(Status.NEW);
            return;
        }
        Status newStatus = Status.NEW;
        for (int subtaskId : editEpic.getSubtasks()) {
            if (allSubtask.get(subtaskId).getStatus() != Status.NEW) {
                newStatus = Status.IN_PROGRESS;
                break;
            }
        }
        if (newStatus == Status.NEW) {
            editEpic.setStatus(Status.NEW);
            return;
        }

        for (int subtaskId : editEpic.getSubtasks()) {
            if (allSubtask.get(subtaskId).getStatus() != Status.DONE) {
                editEpic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        editEpic.setStatus(Status.DONE);
    }

    private void updateEpicTime(int epicId) {
        Epic editEpic = allEpic.get(epicId);

        LocalDateTime startTime = null; //дата старта самой ранней подзадачи
        LocalDateTime endTime = null; //время завершения — время окончания самой поздней из подзадач
        long duration = 0; //сумма продолжительностей всех его подзадач

        for (int subtaskId : editEpic.getSubtasks()) {
            Subtask subtask = allSubtask.get(subtaskId);
            if (startTime == null || startTime.isAfter(subtask.getStartTime())) {
                startTime = subtask.getStartTime();
            }

            if (endTime == null || endTime.isBefore(subtask.getEndTime())) {
                endTime = subtask.getEndTime();
            }

            duration += subtask.getDuration().toMinutes();
        }

        if (startTime != null) editEpic.setStartTime(startTime);
        if (endTime != null) editEpic.setEndTime(endTime);
        editEpic.setDuration(Duration.ofMinutes(duration));
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortTasksByStartTime);
    }

    private boolean cheakTaskByStartTime(Task t1, Task t2) {
        return t1.getStartTime().equals(t2.getStartTime()) ||
               t1.getEndTime().equals(t2.getEndTime()) ||
               t1.getStartTime().isBefore(t2.getStartTime()) && t1.getEndTime().isAfter(t2.getStartTime()) ||
               t1.getStartTime().isBefore(t2.getEndTime()) && t1.getEndTime().isAfter(t2.getEndTime());
    }
}
