import javax.swing.*;
import java.awt.*;

public class Main {
    private static UserManager userManager = new UserManager();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("ToDoリスト");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new CardLayout());

        LoginPanel loginPanel = new LoginPanel(panel);
        RegisterPanel registerPanel = new RegisterPanel(panel);
        DashboardPanel dashboardPanel = new DashboardPanel(panel);

        panel.add(loginPanel.getPanel(), "ログイン");
        panel.add(registerPanel.getPanel(), "新規登録");
        panel.add(dashboardPanel.getPanel(), "ダッシュボード");

        frame.add(panel);
        frame.setVisible(true);
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
}











