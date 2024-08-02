import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class TaskManager {
    int Identifier = 0;

    HashMap<String, HashMap<Integer, Task>> allTypeTask;

    public TaskManager() {
        allTypeTask = new HashMap<>();
        allTypeTask.put("Task", new HashMap<Integer, Task>());
        allTypeTask.put("Epic", new HashMap<Integer, Task>());
        allTypeTask.put("Subtask", new HashMap<Integer, Task>());
    }

    //получать задачи по типу
    public ArrayList<Task> getAllTasks(){
        return this.getAllTaskByType("Task");
    }

    public ArrayList<Epic> getAllEpics(){
        ArrayList<Epic> allTasks = new ArrayList<Epic>();
        ArrayList<Task> objList = this.getAllTaskByType("Epic");
        for (Task obj : objList) {
            allTasks.add( (Epic)obj );
        }
        return allTasks;
    }

    public ArrayList<Subtask> getAllSubtasks(){
        ArrayList<Subtask> allTasks = new ArrayList<Subtask>();
        ArrayList<Task> objList = this.getAllTaskByType("Subtask");
        for (Task obj : objList) {
            allTasks.add( (Subtask)obj );
        }
        return allTasks;
    }

    public ArrayList<Task> getAllTaskByType(String typeTask){
        ArrayList<Task> allTaskByType = new ArrayList<Task>();
        HashMap<Integer, Task> hasTask = allTypeTask.get(typeTask);
        for (Integer key : hasTask.keySet()) {
            allTaskByType.add(hasTask.get(key));
        }
        return allTaskByType;
    }

    //Удаление всех задач
    public void deleteAllTasks(){
        allTypeTask.remove("Task");
        allTypeTask.put("Task", new HashMap<Integer, Task>());
    }
    public void deleteAllEpics(){
        for (Epic epic : getAllEpics()){
            for (Subtask subtask : epic.subtasks){
                allTypeTask.get("Subtask").remove(subtask.idTask);
            }
        }
        allTypeTask.remove("Epic");
        allTypeTask.put("Epic", new HashMap<Integer, Task>());
    }
    public void deleteAllSubtasks(){
        ArrayList<Epic> epics = new ArrayList<Epic>();
        for (Subtask subtask : getAllSubtasks()){
            subtask.epic.delSubTask(subtask);
        }
        allTypeTask.remove("Subtask");
        allTypeTask.put("Subtask", new HashMap<Integer, Task>());
    }


    //получать задачи по идентификатору
     public Task getTaskById(int id){
        for (String key : allTypeTask.keySet()) {
            HashMap<Integer, Task> hasTask = allTypeTask.get(key);
            if (hasTask.containsKey(id)) return hasTask.get(id);
        }
         return null;
     }

    //Создание. Сам объект должен передаваться в качестве параметра.
    public Task addTask(Task task){
        Identifier++;
        task.idTask = Identifier;
        this.getHasIdTaskByType(task).put(Identifier, task);
        //Если добавился Epic, то надо добавить его Subtask
        if (task.getClassName().equals("Epic")) {
            Epic newTask = (Epic) task;
            if (!newTask.subtasks.isEmpty()) {
                for (Subtask subtask : newTask.subtasks) {
                    if (getTaskById(subtask.idTask) == null) addTask(subtask);
                }
            }
        //Если добавился Subtask, то надо проверить, что добавлен его Epic
        } else if (task.getClassName().equals("Subtask")) {
            Subtask newTask = (Subtask) task;
            if (getTaskById(newTask.epic.idTask) == null){
                addTask(newTask.epic);
            }
        }
        return task;
    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public Task updateTask(Task task){
        int id = task.idTask;
        HashMap<Integer, Task> hasIdTask = this.getHasIdTaskByType(task);
        if (hasIdTask.containsKey(id)) {
            hasIdTask.put(id, task);
            task.setStatus(task.status);
        }
        return task;
    }


    private HashMap<Integer, Task> getHasIdTaskByType( Task task ){
        return allTypeTask.get(task.getClassName());
    }

    //Удаление по идентификатору.
    public void deleteTaskById(int id) {
        for (String key : allTypeTask.keySet()) {
            if (allTypeTask.get(key).containsKey(id)) {
                //Если удаляется большая задача(Epic), то надо удалить его Subtask
                if (key.equals("Epic")) {
                    Epic delEpic = (Epic) allTypeTask.get(key).get(id);
                    for (Subtask subtask : delEpic.subtasks){
                        deleteTaskByIdAndType(subtask.idTask, "Subtask");
                    }
                //Если удаляется подзадача(Subtask) надо проверять статус у большой задачи Epic
                } else if (key.equals("Subtask")) {
                    Subtask delSubtask = (Subtask) allTypeTask.get(key).get(id);
                    delSubtask.epic.delSubTask(delSubtask);
                }
                allTypeTask.get(key).remove(id);

            }

        }
    }

    private void deleteTaskByIdAndType(int id, String type){
        allTypeTask.get(type).remove(id);
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
       return epic.getSubtasks();
    }

    @Override
    public String toString() {
        String retStr = "";
        for (String key : allTypeTask.keySet()) {
            retStr += key + ": " + "\n";
            if (!allTypeTask.get(key).isEmpty()) {

                for (Task task : allTypeTask.get(key).values()) {
                    retStr += task.toString() + "\n";
                }
            }
            retStr += "\n";
        }
        return retStr;
    }

}
