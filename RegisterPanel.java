import javax.swing.*;
import java.awt.*;

public class RegisterPanel {
    private JPanel registerPanel;
    private JPanel mainPanel;

    public RegisterPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        createRegisterPanel();
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("ユーザー名: ");
        JTextField userText = new JTextField(20);

        JLabel passwordLabel = new JLabel("パスワード: ");
        JPasswordField passwordText = new JPasswordField(20);

        JButton registerButton = new JButton("新規登録");
        registerButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            UserManager userManager = Main.getUserManager();
            if (userManager.register(username, password)) {
                JOptionPane.showMessageDialog(mainPanel, "ご登録ありがとうございました。");
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "ログイン");
            } else {
                JOptionPane.showMessageDialog(mainPanel, "ユーザー名が既に登録されています。");
            }
        });

        JButton backButton = new JButton("戻る");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "ログイン");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        registerPanel.add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        registerPanel.add(passwordText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(registerButton, gbc);

        gbc.gridx = 1;
        registerPanel.add(backButton, gbc);
    }

    public JPanel getPanel() {
        return registerPanel;
    }
}

