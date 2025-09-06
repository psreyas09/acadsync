package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Faculty;
import models.Subject;
import utils.DBHelper;

public class FacultyListView extends JFrame {
    public FacultyListView() {
        setTitle("Faculty and Subject Assignments");
        setSize(700, 400);
        setLocationRelativeTo(null);

        // Create table with faculty and their assigned subjects
        String[] cols = {"Faculty Name", "Available From", "Available To", "Assigned Subjects"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try {
            List<Faculty> faculties = DBHelper.getAllFaculties();
            for (Faculty faculty : faculties) {
                StringBuilder subjectsStr = new StringBuilder();
                List<Subject> subjects = faculty.getAssignedSubjects();
                
                for (int i = 0; i < subjects.size(); i++) {
                    subjectsStr.append(subjects.get(i).getName());
                    if (i < subjects.size() - 1) {
                        subjectsStr.append(", ");
                    }
                }
                
                String[] row = {
                    faculty.getName(),
                    faculty.getAvailableFrom(),
                    faculty.getAvailableTo(),
                    subjectsStr.toString()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading faculty data: " + ex.getMessage());
        }

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Faculty Name
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Available From
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Available To
        table.getColumnModel().getColumn(3).setPreferredWidth(350); // Assigned Subjects

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add a close button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
