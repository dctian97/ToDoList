import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Properties;

public class UserManager {
    private HashMap<String, User> users;
    private static final String USER_FILE_NAME = "users.txt";
    private static final String SETTINGS_FILE_NAME = "settings.properties";

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
        loadSettings();
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        } else {
            User user = new User(username, password);
            users.put(username, user);
            saveUsers();
            return true;
        }
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            user.clearToDoList();
            loadTasks(user);
            return user;
        } else {
            return null;
        }
    }

    private void loadUsers() {
        File file = new File(USER_FILE_NAME);
        if (!file.exists()) {
            System.out.println("ユーザーデータが見つかりません。");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE_NAME))) {
            for (User user : users.values()) {
                bw.write(user.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTasks(User user) {
        String fileName = user.getUsername() + "_tasks.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (ToDoItem item : user.getTodolist()) {
                bw.write(item.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasks(User user) {
        String fileName = user.getUsername() + "_tasks.txt";
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("タスクデータが見つかりません。");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 3) {
                    ToDoItem item = new ToDoItem(parts[0], LocalDate.parse(parts[1]));
                    item.setCompleted(Boolean.parseBoolean(parts[2]));
                    user.addToDoItem(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(User user, ToDoItem item) {
        user.getTodolist().remove(item);
        saveTasks(user);
    }

    public void saveSettings(String username) {
        Properties props = new Properties();
        props.setProperty("lastUser", username);
        try (FileOutputStream out = new FileOutputStream(SETTINGS_FILE_NAME)) {
            props.store(out, "User Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadSettings() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(SETTINGS_FILE_NAME)) {
            props.load(in);
            return props.getProperty("lastUser");
        } catch (IOException e) {
            System.out.println("設定ファイルが見つかりません。");
            return null;
        }
    }
}





