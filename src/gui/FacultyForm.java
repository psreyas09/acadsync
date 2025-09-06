package gui;

import utils.DBHelper;
import javax.swing.*;
import java.awt.*;

public class FacultyForm extends JFrame {
    public FacultyForm() {
        setTitle("Add Faculty");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JTextField nameField = new JTextField();
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JButton saveBtn = new JButton("Save");

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Available From:"));
        panel.add(fromField);
        panel.add(new JLabel("Available To:"));
        panel.add(toField);
        panel.add(new JLabel());
        panel.add(saveBtn);

        add(panel);

        saveBtn.addActionListener(e -> {
            try {
                DBHelper.addFaculty(nameField.getText(), fromField.getText(), toField.getText());
                JOptionPane.showMessageDialog(this, "✅ Faculty added!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠ Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}