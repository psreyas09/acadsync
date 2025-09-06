package gui;

import core.TimetableManager;
import java.awt.*;
import javax.swing.*;

public class DashboardGUI extends JFrame {
    private TimetableManager manager;

    public DashboardGUI() {
        setTitle("AcadSync - Dashboard");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        manager = new TimetableManager();

        // Add buttons
        JButton addFacultyBtn = new JButton("Add Faculty");
        JButton addSubjectBtn = new JButton("Add Subject");
        JButton addRoomBtn = new JButton("Add Room");
        
        // Management buttons
        JButton manageFacultyBtn = new JButton("Manage Faculty");
        JButton manageSubjectBtn = new JButton("Manage Subjects");
        JButton manageRoomBtn = new JButton("Manage Rooms");
        
        // View buttons
        JButton viewFacultyBtn = new JButton("View Faculty & Assignments");
        JButton generateBtn = new JButton("Generate Timetable");
        JButton viewBtn = new JButton("View Timetable");
        JButton roomTimetableBtn = new JButton("View Room Timetables");
        JButton logoutBtn = new JButton("Logout");

        // Set button colors for better organization
        addFacultyBtn.setBackground(new Color(60, 179, 113));
        addSubjectBtn.setBackground(new Color(60, 179, 113));
        addRoomBtn.setBackground(new Color(60, 179, 113));
        addFacultyBtn.setForeground(Color.WHITE);
        addSubjectBtn.setForeground(Color.WHITE);
        addRoomBtn.setForeground(Color.WHITE);
        
        manageFacultyBtn.setBackground(new Color(70, 130, 180));
        manageSubjectBtn.setBackground(new Color(70, 130, 180));
        manageRoomBtn.setBackground(new Color(70, 130, 180));
        manageFacultyBtn.setForeground(Color.WHITE);
        manageSubjectBtn.setForeground(Color.WHITE);
        manageRoomBtn.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new GridLayout(12, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Add section labels
        JLabel addLabel = new JLabel("📝 Add New Items", SwingConstants.CENTER);
        addLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(addLabel);
        panel.add(addFacultyBtn);
        panel.add(addSubjectBtn);
        panel.add(addRoomBtn);
        
        JLabel manageLabel = new JLabel("🔧 Manage Existing Items", SwingConstants.CENTER);
        manageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(manageLabel);
        panel.add(manageFacultyBtn);
        panel.add(manageSubjectBtn);
        panel.add(manageRoomBtn);
        
        JLabel viewLabel = new JLabel("👁️ View & Generate", SwingConstants.CENTER);
        viewLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(viewLabel);
        panel.add(viewFacultyBtn);
        panel.add(generateBtn);
        panel.add(viewBtn);
        panel.add(roomTimetableBtn);
        panel.add(logoutBtn);

        add(panel, BorderLayout.CENTER);

        // Actions for Add buttons
        addFacultyBtn.addActionListener(e -> new FacultyForm());
        addSubjectBtn.addActionListener(e -> new SubjectForm());
        addRoomBtn.addActionListener(e -> new RoomForm());
        
        // Actions for Management buttons
        manageFacultyBtn.addActionListener(e -> new FacultyManagement());
        manageSubjectBtn.addActionListener(e -> new SubjectManagement());
        manageRoomBtn.addActionListener(e -> new RoomManagement());
        
        // Actions for View buttons
        viewFacultyBtn.addActionListener(e -> new FacultyListView());

        generateBtn.addActionListener(e -> {
            manager.generateTimetable();
            JOptionPane.showMessageDialog(this, "✅ Timetable generated!");
        });

        viewBtn.addActionListener(e -> new TimetableView(manager));

        roomTimetableBtn.addActionListener(e -> new RoomTimetableView(manager));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        setVisible(true);
    }
}
