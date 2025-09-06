package gui;

import utils.DBHelper;
import models.Faculty;
import models.Subject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FacultyManagement extends JFrame {
    private JTable facultyTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn, closeBtn;

    public FacultyManagement() {
        setTitle("Faculty Management");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadFacultyData();

        setVisible(true);
    }

    private void initializeComponents() {
        // Create table with columns
        String[] columns = {"ID", "Name", "Available From", "Available To", "Assigned Subjects"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        facultyTable = new JTable(tableModel);
        facultyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        facultyTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        facultyTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        facultyTable.getColumnModel().getColumn(2).setPreferredWidth(100); // From
        facultyTable.getColumnModel().getColumn(3).setPreferredWidth(100); // To
        facultyTable.getColumnModel().getColumn(4).setPreferredWidth(400); // Subjects

        // Create buttons
        addBtn = new JButton("Add Faculty");
        editBtn = new JButton("Edit Faculty");
        deleteBtn = new JButton("Delete Faculty");
        refreshBtn = new JButton("Refresh");
        closeBtn = new JButton("Close");

        // Set button colors
        addBtn.setBackground(new Color(60, 179, 113));
        addBtn.setForeground(Color.WHITE);
        editBtn.setBackground(new Color(70, 130, 180));
        editBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 20, 60));
        deleteBtn.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        // Main table area
        JScrollPane scrollPane = new JScrollPane(facultyTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Select a faculty member to edit or delete"));
        add(infoPanel, BorderLayout.NORTH);
    }

    private void setupEventHandlers() {
        addBtn.addActionListener(e -> {
            new FacultyForm();
            loadFacultyData(); // Refresh after adding
        });

        editBtn.addActionListener(e -> editSelectedFaculty());

        deleteBtn.addActionListener(e -> deleteSelectedFaculty());

        refreshBtn.addActionListener(e -> loadFacultyData());

        closeBtn.addActionListener(e -> dispose());

        // Enable/disable buttons based on selection
        facultyTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = facultyTable.getSelectedRow() != -1;
            editBtn.setEnabled(hasSelection);
            deleteBtn.setEnabled(hasSelection);
        });
    }

    private void loadFacultyData() {
        tableModel.setRowCount(0); // Clear existing data
        
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
                
                Object[] row = {
                    faculty.getId(),
                    faculty.getName(),
                    faculty.getAvailableFrom(),
                    faculty.getAvailableTo(),
                    subjectsStr.toString()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading faculty data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedFaculty() {
        int selectedRow = facultyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a faculty member to edit.");
            return;
        }

        int facultyId = (int) tableModel.getValueAt(selectedRow, 0);
        new FacultyEditForm(facultyId, this);
    }

    private void deleteSelectedFaculty() {
        int selectedRow = facultyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a faculty member to delete.");
            return;
        }

        String facultyName = (String) tableModel.getValueAt(selectedRow, 1);
        int facultyId = (int) tableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete faculty member '" + facultyName + "'?\n" +
                "This will also remove all subject assignments for this faculty.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                DBHelper.deleteFaculty(facultyId);
                JOptionPane.showMessageDialog(this, "✅ Faculty member deleted successfully!");
                loadFacultyData(); // Refresh the table
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠ Error deleting faculty: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        loadFacultyData();
    }
}
