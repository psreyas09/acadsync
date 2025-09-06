package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Subject;
import utils.DBHelper;

public class SubjectManagement extends JFrame {
    private JTable subjectTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, deleteBtn, refreshBtn, closeBtn;

    public SubjectManagement() {
        setTitle("Subject Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadSubjectData();

        setVisible(true);
    }

    private void initializeComponents() {
        // Create table with columns
        String[] columns = {"ID", "Subject Name", "Hours/Week", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        subjectTable = new JTable(tableModel);
        subjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        subjectTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Name
        subjectTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Hours
        subjectTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Type

        // Create buttons
        addBtn = new JButton("Add Subject");
        deleteBtn = new JButton("Delete Subject");
        refreshBtn = new JButton("Refresh");
        closeBtn = new JButton("Close");

        // Set button colors
        addBtn.setBackground(new Color(60, 179, 113));
        addBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 20, 60));
        deleteBtn.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        // Main table area
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Subject Management - Select a subject to delete"));
        add(infoPanel, BorderLayout.NORTH);
    }

    private void setupEventHandlers() {
        addBtn.addActionListener(e -> {
            new SubjectForm();
            loadSubjectData(); // Refresh after adding
        });

        deleteBtn.addActionListener(e -> deleteSelectedSubject());

        refreshBtn.addActionListener(e -> loadSubjectData());

        closeBtn.addActionListener(e -> dispose());

        // Enable/disable delete button based on selection
        subjectTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = subjectTable.getSelectedRow() != -1;
            deleteBtn.setEnabled(hasSelection);
        });
    }

    private void loadSubjectData() {
        tableModel.setRowCount(0); // Clear existing data
        
        try {
            List<Subject> subjects = DBHelper.getAllSubjects();
            for (Subject subject : subjects) {
                Object[] row = {
                    subject.getId(),
                    subject.getName(),
                    subject.getHoursPerWeek(),
                    subject.isOnline() ? "Online" : "Offline"
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading subject data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to delete.");
            return;
        }

        String subjectName = (String) tableModel.getValueAt(selectedRow, 1);
        int subjectId = (int) tableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete subject '" + subjectName + "'?\n" +
                "This will also remove all faculty assignments for this subject.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                DBHelper.deleteSubject(subjectId);
                JOptionPane.showMessageDialog(this, "✅ Subject deleted successfully!");
                loadSubjectData(); // Refresh the table
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠ Error deleting subject: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
