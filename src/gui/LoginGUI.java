package gui;

import utils.DBHelper;
import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginGUI() {
        setTitle("AcadSync - Admin Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        statusLabel = new JLabel("Enter your credentials", SwingConstants.CENTER);

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            try {
                if (DBHelper.authenticateAdmin(user, pass)) {
                    dispose(); // close login
                    new DashboardGUI(); // open dashboard
                } else {
                    statusLabel.setText("❌ Invalid credentials");
                }
            } catch (Exception ex) {
                statusLabel.setText("⚠ Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}