import javax.swing.*;
import java.awt.*;

public class LoginPanel {
    private JPanel loginPanel;
    private JPanel mainPanel;

    public LoginPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        createLoginPanel();
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("ユーザー名: ");
        JTextField userText = new JTextField(20);

        JLabel passwordLabel = new JLabel("パスワード: ");
        JPasswordField passwordText = new JPasswordField(20);

        JButton loginButton = new JButton("ログイン");
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            UserManager userManager = Main.getUserManager();
            User loggedInUser = userManager.login(username, password);
            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(mainPanel, "ログイン成功しました。");
                Main.setLoggedInUser(loggedInUser);
                userManager.saveSettings(loggedInUser.getUsername());
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "ダッシュボード");
                DashboardPanel.updateTodoList(loggedInUser.getTodolist(), false);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "ユーザー名かパスワードが無効です。");
            }
        });

        JButton registerButton = new JButton("新規登録");
        registerButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "新規登録");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        loginPanel.add(registerButton, gbc);
    }

    public JPanel getPanel() {
        return loginPanel;
    }
}

