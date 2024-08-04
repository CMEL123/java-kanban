package model;

public enum Status {
    NEW,          // только создана, но к её выполнению ещё не приступили
    IN_PROGRESS,  //над задачей ведётся работа.
    DONE,         //задача выполнена.
}
