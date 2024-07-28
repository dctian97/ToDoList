import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPanel {
    private JPanel dashboardPanel;
    private JPanel mainPanel;
    private static JPanel incompletePanel;
    private static JPanel completeTaskPanel;
    private static JTextField searchField;

    public DashboardPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        createDashboardPanel();
    }

    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());

        incompletePanel = new JPanel();
        incompletePanel.setLayout(new BoxLayout(incompletePanel, BoxLayout.Y_AXIS));
        JScrollPane incompleteScrollPane = new JScrollPane(incompletePanel);

        completeTaskPanel = new JPanel();
        completeTaskPanel.setLayout(new BoxLayout(completeTaskPanel, BoxLayout.Y_AXIS));
        JScrollPane completeScrollPane = new JScrollPane(completeTaskPanel);

        JPanel taskPanel = new JPanel(new GridLayout(2, 1));

        JPanel incompleteContainer = new JPanel(new BorderLayout());
        JLabel incompleteLabel = new JLabel("未完了タスク");
        incompleteContainer.add(incompleteLabel, BorderLayout.NORTH);
        incompleteContainer.add(incompleteScrollPane, BorderLayout.CENTER);

        JPanel completeContainer = new JPanel(new BorderLayout());
        JLabel completeLabel = new JLabel("完了タスク");
        completeContainer.add(completeLabel, BorderLayout.NORTH);
        completeContainer.add(completeScrollPane, BorderLayout.CENTER);

        taskPanel.add(incompleteContainer);
        taskPanel.add(completeContainer);

        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchTasks();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchTasks();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchTasks();
            }
            public void searchTasks() {
                String searchText = searchField.getText().toLowerCase();
                List<ToDoItem> filteredTasks = Main.getLoggedInUser().getTodolist().stream()
                        .filter(item -> item.getTask().toLowerCase().contains(searchText))
                        .collect(Collectors.toList());
                updateTodoList(filteredTasks, true);
            }
        });

        JButton addButton = new JButton("タスク追加");
        addButton.addActionListener(e -> {
            JTextField taskField = new JTextField(20);
            JTextField dateField = new JTextField(10);
            JPanel inputPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            inputPanel.add(new JLabel("タスク: "), gbc);

            gbc.gridx = 1;
            inputPanel.add(taskField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            inputPanel.add(new JLabel("日付(yyyy-mm-dd): "), gbc);

            gbc.gridx = 1;
            inputPanel.add(dateField, gbc);

            int result = JOptionPane.showConfirmDialog(mainPanel, inputPanel, "新規タスク", JOptionPane.OK_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String task = taskField.getText();
                LocalDate date = parseDate(dateField.getText());
                if (date != null) {
                    ToDoItem item = new ToDoItem(task, date);
                    Main.getLoggedInUser().addToDoItem(item);
                    Main.getUserManager().saveTasks(Main.getLoggedInUser());
                    updateTodoList(Main.getLoggedInUser().getTodolist(), false);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "日付が間違っています。入力し直してください(yyyy-mm-dd)。");
                }
            }
        });

        JButton logoutButton = new JButton("ログアウト");
        logoutButton.addActionListener(e -> {
            Main.getUserManager().saveSettings(Main.getLoggedInUser().getUsername());
            Main.setLoggedInUser(null);
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "ログイン");
        });

        dashboardPanel.add(searchField, BorderLayout.NORTH);
        dashboardPanel.add(taskPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(addButton);
        buttonPanel.add(logoutButton);
        dashboardPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    public static void updateTodoList(List<ToDoItem> tasks, boolean isSearch) {
        incompletePanel.removeAll();
        completeTaskPanel.removeAll();

        for (ToDoItem item : tasks) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            JCheckBox completedCheckBox = new JCheckBox("", item.isCompleted());
            completedCheckBox.addActionListener(e -> {
                item.setCompleted(completedCheckBox.isSelected());
                Main.getUserManager().saveTasks(Main.getLoggedInUser());
                updateTodoList(Main.getLoggedInUser().getTodolist(), false);
            });
            JLabel taskLabel = new JLabel(item.getTask() + ", " + item.getDate());
            taskLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    editTask(item);
                }
            });
            JButton deleteButton = new JButton("削除");
            deleteButton.addActionListener(e -> {
                Main.getUserManager().deleteTask(Main.getLoggedInUser(), item);
                updateTodoList(Main.getLoggedInUser().getTodolist(), false);
            });

            itemPanel.add(completedCheckBox, BorderLayout.WEST);
            itemPanel.add(taskLabel, BorderLayout.CENTER);
            itemPanel.add(deleteButton, BorderLayout.EAST);

            if (item.isCompleted()) {
                completeTaskPanel.add(itemPanel);
            } else {
                incompletePanel.add(itemPanel);
            }
        }

        incompletePanel.revalidate();
        incompletePanel.repaint();
        completeTaskPanel.revalidate();
        completeTaskPanel.repaint();

        if (!isSearch) {
            LocalDate today = LocalDate.now();
            for (ToDoItem item : Main.getLoggedInUser().getTodolist()) {
                if (!item.isCompleted() && item.getDate().isBefore(today)) {
                    JOptionPane.showMessageDialog(null, "期限が過ぎたタスクがあります: " + item.getTask(), "お知らせ", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    private static void editTask(ToDoItem item) {
        JTextField taskField = new JTextField(item.getTask(), 20);
        JTextField dateField = new JTextField(item.getDate().toString(), 10);
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("タスク: "), gbc);

        gbc.gridx = 1;
        inputPanel.add(taskField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("日付(yyyy-mm-dd): "), gbc);

        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "タスク編集", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String task = taskField.getText();
            LocalDate date = parseDate(dateField.getText());
            if (date != null) {
                item.setTask(task);
                item.setDate(date);
                Main.getUserManager().saveTasks(Main.getLoggedInUser());
                updateTodoList(Main.getLoggedInUser().getTodolist(), false);
            } else {
                JOptionPane.showMessageDialog(null, "日付が間違っています。入力し直してください(yyyy-mm-dd)。");
            }
        }
    }
    private static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public JPanel getPanel() {
        return dashboardPanel;
    }
}

