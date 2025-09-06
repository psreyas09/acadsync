package gui;

import utils.DBHelper;
import javax.swing.*;
import java.awt.*;

public class RoomForm extends JFrame {
    public RoomForm() {
        setTitle("Add Room");
        setSize(300, 150);
        setLocationRelativeTo(null);

        JTextField roomNoField = new JTextField();
        JTextField capacityField = new JTextField();
        JButton saveBtn = new JButton("Save");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Room No:"));
        panel.add(roomNoField);
        panel.add(new JLabel("Capacity:"));
        panel.add(capacityField);
        panel.add(new JLabel());
        panel.add(saveBtn);

        add(panel);

        saveBtn.addActionListener(e -> {
            try {
                DBHelper.addRoom(roomNoField.getText(),
                        Integer.parseInt(capacityField.getText()));
                JOptionPane.showMessageDialog(this, "✅ Room added!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠ Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}