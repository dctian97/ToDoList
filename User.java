import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<ToDoItem> todolist;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.todolist = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void addToDoItem(ToDoItem item) {
        todolist.add(item);
    }

    public ArrayList<ToDoItem> getTodolist() {
        return todolist;
    }

    public void clearToDoList() {
        todolist.clear();
    }

    public String toString() {
        return username + "," + password;
    }
}

