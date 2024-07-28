import java.time.LocalDate;

public class ToDoItem {
    private String task;
    private LocalDate date;
    private boolean isCompleted;

    public ToDoItem(String task, LocalDate date) {
        this.task = task;
        this.date = date;
        this.isCompleted = false;
    }

    public String getTask() {
        return task;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String toString() {
        return task + ", " + date + ", " + isCompleted;
    }
}



