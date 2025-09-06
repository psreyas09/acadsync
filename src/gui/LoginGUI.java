package gui;

import java.awt.*;
import javax.swing.*;
import utils.DBHelper;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private JLabel statusLabel;

    public LoginGUI() {
        setTitle("AcadSync - Admin Login");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Admin Account");
        statusLabel = new JLabel("Enter your credentials", SwingConstants.CENTER);

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                statusLabel.setText("❌ Please enter both username and password");
                return;
            }

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

        createAccountButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                statusLabel.setText("❌ Please enter username and password to create account");
                return;
            }

            try {
                DBHelper.addAdmin(user, pass);
                statusLabel.setText("✅ Admin account created! You can now login.");
                JOptionPane.showMessageDialog(this, "Admin account created successfully!\nUsername: " + user + "\nYou can now login with these credentials.");
            } catch (Exception ex) {
                if (ex.getMessage().contains("UNIQUE constraint")) {
                    statusLabel.setText("❌ Username already exists");
                } else {
                    statusLabel.setText("⚠ Error: " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }
}