import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        System.out.println("Test1");
        Task t1 = new Task("Задача1", "Задача1d", 1234);
        Task t2 = new Task("Задача2", "Задача2d", 12345, Status.IN_PROGRESS);

        System.out.println(t1.toString());
        System.out.println(t2.toString());

        puts("=" , 30);
        System.out.println("Test2");
        Epic e1 = new Epic("Эпик1", "Задача1d", 1424);
        Epic e2 = new Epic("Эпик2", "Задача2d", 4343);

        System.out.println(e1.toString());
        System.out.println(e2.toString());

        puts("=" , 30);
        System.out.println("Test3");
        Subtask s1 = new Subtask("Подзадача1", "Задача1d", 1424, e1, Status.IN_PROGRESS);
        Subtask s2 = new Subtask("Подзадача2", "Задача2d", 4343, e1, Status.NEW);
        Subtask s3 = new Subtask("Подзадача3", "Задача1d", 1424, e2, Status.DONE);

        System.out.println(s1.toString());
        System.out.println(s2.toString());
        System.out.println(s3.toString());

        System.out.println(e1.toString());
        System.out.println(e2.toString());

        puts("=" , 30);
        System.out.println("Test4");

        TaskManager manager = new TaskManager();
        System.out.println(manager.toString());

        System.out.println("Test5: Добавление + Печать");

        manager.addTask(t1);
        manager.addTask(t2);
        manager.addTask(e1);
        manager.addTask(s3);
        System.out.println(manager.toString());
        puts("=" , 30);

        System.out.println("Test6: Получение списка всех задач");

        System.out.println("Tasks:");
        System.out.println(manager.getAllTasks());
        System.out.println("Epics:");
        System.out.println(manager.getAllEpics());
        System.out.println("Subtasks:");
        System.out.println(manager.getAllSubtasks());

        puts("=" , 30);

        System.out.println("Test7: Удаление всех задач");
        /*
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        System.out.println(manager.toString());
        */

        System.out.println("Test8: Получение по идентификатору.");

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(4));
        System.out.println(manager.getTaskById(5));

        puts("=" , 30);

        //Не очень понял, как это должно работать,
        //на QA_04 объяснили, что берем объект смотрим его id,
        // если такой id уже есть, то заменяем
        System.out.println("Test9: Обновление.");
        Task t3 = new Task("Задача3", "Задача1d", 2);
        manager.updateTask(t3);
        System.out.println(manager.getAllTasks());
        puts("=" , 30);

        System.out.println("Test10: Удаление по идентификатору.");
        //manager.deleteTaskById(2);
        //manager.deleteTaskById(3);
        //manager.deleteTaskById(6);
       // System.out.println(manager.toString());
        puts("=" , 30);


        System.out.println("Test11: Получение списка всех подзадач определённого эпика.");
        System.out.println(manager.getSubtaskByEpic(e1));

        puts("=" , 30);

    }

    public static void puts(String str, int count){
        String putsStr = str;
        for(int i=1; i<count; i++){
            putsStr += str;
        }
        System.out.println(putsStr);
    }

}