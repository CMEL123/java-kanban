package service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ManagersTest {

    //убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    void getDefault() {
        TaskManager taskManager = Managers.getDefault();
        Assertions.assertTrue(taskManager instanceof TaskManager);
        Assertions.assertNotNull(taskManager);
    }

    @Test
    void getDefaultHistory() {
        HistoryManager taskManager = Managers.getDefaultHistory();
        Assertions.assertTrue(taskManager instanceof HistoryManager);
        Assertions.assertNotNull(taskManager);
    }

}