package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import models.Faculty;
import models.Subject;
import utils.DBHelper;

public class FacultyEditForm extends JFrame {
    private int facultyId;
    private FacultyManagement parentWindow;
    private JTextField nameField;
    private JTextField fromField;
    private JTextField toField;
    private JList<Subject> availableSubjectsList;
    private JList<Subject> selectedSubjectsList;
    private DefaultListModel<Subject> availableModel;
    private DefaultListModel<Subject> selectedModel;
    private Faculty currentFaculty;

    public FacultyEditForm(int facultyId, FacultyManagement parent) {
        this.facultyId = facultyId;
        this.parentWindow = parent;
        
        setTitle("Edit Faculty");
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initializeComponents();
        setupLayout();
        loadFacultyData();
        loadSubjects();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeComponents() {
        nameField = new JTextField();
        fromField = new JTextField();
        toField = new JTextField();
        
        availableModel = new DefaultListModel<>();
        selectedModel = new DefaultListModel<>();
        availableSubjectsList = new JList<>(availableModel);
        selectedSubjectsList = new JList<>(selectedModel);
        
        availableSubjectsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedSubjectsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void setupLayout() {
        // Top panel for basic faculty info
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(new JLabel("Faculty Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        topPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        topPanel.add(new JLabel("Available From:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        topPanel.add(fromField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        topPanel.add(new JLabel("Available To:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        topPanel.add(toField, gbc);
        
        // Middle panel for subject assignment
        JPanel subjectPanel = new JPanel(new BorderLayout());
        subjectPanel.setBorder(BorderFactory.createTitledBorder("Subject Assignment"));
        
        // Available subjects panel
        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.add(new JLabel("Available Subjects:"), BorderLayout.NORTH);
        availablePanel.add(new JScrollPane(availableSubjectsList), BorderLayout.CENTER);
        
        // Selected subjects panel
        JPanel selectedPanel = new JPanel(new BorderLayout());
        selectedPanel.add(new JLabel("Assigned Subjects:"), BorderLayout.NORTH);
        selectedPanel.add(new JScrollPane(selectedSubjectsList), BorderLayout.CENTER);
        
        // Button panel for moving subjects
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton addButton = new JButton("Add →");
        JButton removeButton = new JButton("← Remove");
        JButton addAllButton = new JButton("Add All →");
        JButton removeAllButton = new JButton("← Remove All");
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(addAllButton);
        buttonPanel.add(removeAllButton);
        
        // Layout the subject assignment area
        JPanel subjectContent = new JPanel(new BorderLayout());
        subjectContent.add(availablePanel, BorderLayout.WEST);
        subjectContent.add(buttonPanel, BorderLayout.CENTER);
        subjectContent.add(selectedPanel, BorderLayout.EAST);
        subjectPanel.add(subjectContent, BorderLayout.CENTER);
        
        // Bottom panel for action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton updateBtn = new JButton("Update Faculty");
        JButton cancelBtn = new JButton("Cancel");
        bottomPanel.add(updateBtn);
        bottomPanel.add(cancelBtn);
        
        // Add to main frame
        add(topPanel, BorderLayout.NORTH);
        add(subjectPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Setup button actions
        addButton.addActionListener(e -> moveSubjects(availableSubjectsList, availableModel, selectedModel));
        removeButton.addActionListener(e -> moveSubjects(selectedSubjectsList, selectedModel, availableModel));
        addAllButton.addActionListener(e -> moveAllSubjects(availableModel, selectedModel));
        removeAllButton.addActionListener(e -> moveAllSubjects(selectedModel, availableModel));
        
        updateBtn.addActionListener(e -> updateFaculty());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void loadFacultyData() {
        try {
            List<Faculty> faculties = DBHelper.getAllFaculties();
            for (Faculty faculty : faculties) {
                if (faculty.getId() == facultyId) {
                    currentFaculty = faculty;
                    nameField.setText(faculty.getName());
                    fromField.setText(faculty.getAvailableFrom());
                    toField.setText(faculty.getAvailableTo());
                    break;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading faculty data: " + ex.getMessage());
        }
    }

    private void loadSubjects() {
        try {
            List<Subject> allSubjects = DBHelper.getAllSubjects();
            List<Subject> assignedSubjects = currentFaculty != null ? currentFaculty.getAssignedSubjects() : new ArrayList<>();
            
            availableModel.clear();
            selectedModel.clear();
            
            // Add assigned subjects to selected list
            for (Subject subject : assignedSubjects) {
                selectedModel.addElement(subject);
            }
            
            // Add unassigned subjects to available list
            for (Subject subject : allSubjects) {
                if (!assignedSubjects.contains(subject)) {
                    availableModel.addElement(subject);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading subjects: " + ex.getMessage());
        }
    }

    private void setupEventHandlers() {
        // Add placeholder text hints
        fromField.setToolTipText("e.g., 09:00");
        toField.setToolTipText("e.g., 17:00");
        nameField.setToolTipText("Enter faculty member's full name");
    }

    private void moveSubjects(JList<Subject> sourceList, DefaultListModel<Subject> sourceModel, DefaultListModel<Subject> targetModel) {
        List<Subject> selected = sourceList.getSelectedValuesList();
        for (Subject subject : selected) {
            sourceModel.removeElement(subject);
            targetModel.addElement(subject);
        }
    }

    private void moveAllSubjects(DefaultListModel<Subject> sourceModel, DefaultListModel<Subject> targetModel) {
        while (!sourceModel.isEmpty()) {
            Subject subject = sourceModel.remove(0);
            targetModel.addElement(subject);
        }
    }

    private void updateFaculty() {
        try {
            // Validate input
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter faculty name!");
                return;
            }

            // Update faculty basic info
            DBHelper.updateFaculty(facultyId, nameField.getText().trim(), fromField.getText().trim(), toField.getText().trim());
            
            // Remove all existing subject assignments
            List<Subject> currentAssignments = DBHelper.getSubjectsForFaculty(facultyId);
            for (Subject subject : currentAssignments) {
                DBHelper.removeSubjectFromFaculty(facultyId, subject.getId());
            }
            
            // Add new subject assignments
            for (int i = 0; i < selectedModel.getSize(); i++) {
                Subject subject = selectedModel.getElementAt(i);
                DBHelper.assignSubjectToFaculty(facultyId, subject.getId());
            }
            
            JOptionPane.showMessageDialog(this, "✅ Faculty updated successfully with " + selectedModel.getSize() + " assigned subjects!");
            
            // Refresh parent window and close this dialog
            if (parentWindow != null) {
                parentWindow.refreshData();
            }
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error updating faculty: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
