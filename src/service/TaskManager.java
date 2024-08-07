package service;

import model.*;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int identifier = 0;

    HashMap<Integer, Task> allTask;
    HashMap<Integer, Epic> allEpic;
    HashMap<Integer, Subtask> allSubtask;

    public TaskManager() {
        allTask    =  new HashMap<Integer, Task>();
        allEpic    =  new HashMap<Integer, Epic>();
        allSubtask =  new HashMap<Integer, Subtask>();
    }

    private int getIdentifier(){
        return identifier++;
    }


    //Создание. Сам объект должен передаваться в качестве параметра.
    public Task addTask(Task task){
        task.setIdTask(getIdentifier());
        if (task.getTypeTask() == TypeTask.TASK) {
            allTask.put(task.getIdTask(), task);
        }else if (task.getTypeTask() == TypeTask.EPIC){
            Epic newTask = (Epic) task;
            allEpic.put(task.getIdTask(), newTask);
            //Мне сказали новый эпик, всегда будет с пустым списком подзадач
            //Поэтому на всякий
            newTask.setSubtasks(new ArrayList<Integer>());
        }else if (task.getTypeTask() == TypeTask.SUBTASK){
            Subtask newTask = (Subtask)task;
            //Если в allEpic нет эпика с id, который указан в новой подзадаче,
            // то надо без сохранения завершать метод через return
            if (allEpic.containsKey(newTask.getEpicId())) {
                allSubtask.put(task.getIdTask(), newTask);
                allEpic.get(newTask.getEpicId()).addSubTask(newTask);
                updateEpicStatus(newTask.getEpicId());
            }
        }

        return task;
    }

    //получать задачи по типу
    public ArrayList<Task> getAllTasks(){
        return new ArrayList<Task>(allTask.values());
    }

    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<Epic>(allEpic.values());
    }

    public ArrayList<Subtask> getAllSubtasks(){
        return new ArrayList<Subtask>(allSubtask.values());
    }

    //Удаление всех задач
    public void deleteAllTasks(){
        allTask.clear();
    }
    public void deleteAllEpics(){
        allSubtask.clear();
        allEpic.clear();
    }
    public void deleteAllSubtasks(){
        for (Epic epic : allEpic.values() ) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
        allSubtask.clear();
    }


    //получать задачи по идентификатору
     public Task getTaskById(int id) {
         if (allTask.containsKey(id)) {
             return allTask.get(id);
         }else if (allEpic.containsKey(id)) {
             return ((Task) allEpic.get(id));
         }else if (allSubtask.containsKey(id)) {
             return ((Task) allSubtask.get(id));
         } else {
             return null;
         }
     }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public Task updateTask(Task task){
        int id = task.getIdTask();
        if (task.getTypeTask() == TypeTask.TASK) {
            if (allTask.containsKey(id)) {
                allTask.put(id, task);
            }
        }else if (task.getTypeTask() == TypeTask.EPIC){
            if (allEpic.containsKey(id)) {
                allEpic.put(id, (Epic)task);
            }
        }else if (task.getTypeTask() == TypeTask.SUBTASK){
            if (allSubtask.containsKey(id)) {
                allSubtask.put(id, (Subtask)task);
                updateEpicStatus( ((Subtask) task).getEpicId() );
            }
        }
        return task;
    }

    //Удаление по идентификатору.
    public void deleteTaskById(int id) {
        Task delTask = getTaskById(id);
        if (delTask == null){
            return;
        } else if (delTask.getTypeTask() == TypeTask.TASK) {
            allTask.remove(id);
        }else if (delTask.getTypeTask() == TypeTask.EPIC){
            for (int subtaskId : ((Epic) delTask).getSubtasks()) {
                allSubtask.remove(subtaskId);
            }
            allEpic.remove(id);
        }else if (delTask.getTypeTask() == TypeTask.SUBTASK){
            Subtask newDelTask = (Subtask)delTask;
            Epic epic = allEpic.get(newDelTask.getEpicId());
            epic.delSubTask(newDelTask);
            allSubtask.remove(id);
            updateEpicStatus( epic.getIdTask() );
        }
    }

    //Получение списка всех подзадач определённого эпика.
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
        return retStr;
    }

    private void updateEpicStatus(int epicId ) {
        Epic editEpic = allEpic.get(epicId);
        if (editEpic.getSubtasks().isEmpty()){
            editEpic.setStatus(Status.NEW);
            return ;
        }
        Status new_status = Status.NEW;
        for (int subtaskId : editEpic.getSubtasks()) {
            if ( allSubtask.get(subtaskId).getStatus() != Status.NEW){
                new_status = Status.IN_PROGRESS;
                break;
            }
        }
        if (new_status == Status.NEW) {
            editEpic.setStatus(Status.NEW);
            return ;
        }

        for (int subtaskId : editEpic.getSubtasks()) {
            if ( allSubtask.get(subtaskId).getStatus() != Status.DONE){
                editEpic.setStatus(Status.IN_PROGRESS);
                return ;
            }
        }
        editEpic.setStatus(Status.DONE);
    }
}
