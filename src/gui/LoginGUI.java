package gui;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import utils.DBHelper;

public class LoginGUI extends JFrame {
    // UI Constants for better maintainability
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 280;
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final Color SUCCESS_COLOR = new Color(0, 150, 0);
    private static final Color ERROR_COLOR = new Color(200, 0, 0);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 11);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private JLabel statusLabel;

    public LoginGUI() {
        setTitle("AcadSync - Admin Login");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setVisible(true);
    }

    private void initializeComponents() {
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Admin Account");
        statusLabel = new JLabel("Enter your credentials", SwingConstants.CENTER);

        // Improve component styling
        usernameField.setFont(LABEL_FONT);
        passwordField.setFont(LABEL_FONT);
        loginButton.setFont(BUTTON_FONT);
        createAccountButton.setFont(BUTTON_FONT);
        statusLabel.setFont(LABEL_FONT);
        
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        createAccountButton.setBackground(new Color(60, 179, 113));
        createAccountButton.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 1, 5, 8));
        fieldsPanel.add(new JLabel("Username:"));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(new JLabel("Password:"));
        fieldsPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupEventListeners() {

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            // Enhanced input validation
            if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                showError("❌ Please enter both username and password");
                return;
            }

            if (user.length() < 3) {
                showError("❌ Username must be at least 3 characters long");
                return;
            }

            if (pass.length() < 4) {
                showError("❌ Password must be at least 4 characters long");
                return;
            }

            try {
                if (DBHelper.authenticateAdmin(user.trim(), pass)) {
                    showSuccess("✅ Login successful!");
                    dispose(); // close login
                    new DashboardGUI(); // open dashboard
                } else {
                    showError("❌ Invalid credentials");
                    clearPasswordField();
                }
            } catch (SQLException ex) {
                showError("⚠ Database error: " + ex.getMessage());
            } catch (Exception ex) {
                showError("⚠ Unexpected error: " + ex.getMessage());
            }
        });

        createAccountButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            // Enhanced validation for account creation
            if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                showError("❌ Please enter username and password to create account");
                return;
            }

            if (user.length() < 3) {
                showError("❌ Username must be at least 3 characters long");
                return;
            }

            if (pass.length() < 4) {
                showError("❌ Password must be at least 4 characters long");
                return;
            }

            if (!isValidPassword(pass)) {
                showError("❌ Password must contain letters and numbers");
                return;
            }

            try {
                DBHelper.addAdmin(user.trim(), pass);
                showSuccess("✅ Admin account created! You can now login.");
                JOptionPane.showMessageDialog(this, 
                    "Admin account created successfully!\nUsername: " + user + 
                    "\nYou can now login with these credentials.", 
                    "Account Created", 
                    JOptionPane.INFORMATION_MESSAGE);
                clearPasswordField();
            } catch (SQLException ex) {
                if (ex.getMessage().contains("UNIQUE constraint")) {
                    showError("❌ Username already exists");
                } else {
                    showError("⚠ Database error: " + ex.getMessage());
                }
            } catch (Exception ex) {
                showError("⚠ Unexpected error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    // Helper methods for better code organization
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(ERROR_COLOR);
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(SUCCESS_COLOR);
    }

    private void clearPasswordField() {
        passwordField.setText("");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 4 && 
               password.matches(".*[a-zA-Z].*") && 
               password.matches(".*[0-9].*");
    }
}