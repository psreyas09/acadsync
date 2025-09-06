package gui;

import utils.DBHelper;
import javax.swing.*;
import java.awt.*;

public class SubjectForm extends JFrame {
    public SubjectForm() {
        setTitle("Add Subject");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JTextField nameField = new JTextField();
        JTextField hoursField = new JTextField();
        JCheckBox onlineBox = new JCheckBox("Online?");
        JButton saveBtn = new JButton("Save");

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Hours/Week:"));
        panel.add(hoursField);
        panel.add(new JLabel());
        panel.add(onlineBox);
        panel.add(new JLabel());
        panel.add(saveBtn);

        add(panel);

        saveBtn.addActionListener(e -> {
            try {
                DBHelper.addSubject(nameField.getText(),
                        Integer.parseInt(hoursField.getText()),
                        onlineBox.isSelected());
                JOptionPane.showMessageDialog(this, "✅ Subject added!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠ Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}