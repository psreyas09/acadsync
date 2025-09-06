package gui;

import core.TimetableManager;
import javax.swing.*;
import java.awt.*;

public class DashboardGUI extends JFrame {
    private TimetableManager manager;

    public DashboardGUI() {
        setTitle("AcadSync - Dashboard");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        manager = new TimetableManager();

        JButton addFacultyBtn = new JButton("Add Faculty");
        JButton addSubjectBtn = new JButton("Add Subject");
        JButton addRoomBtn = new JButton("Add Room");
        JButton generateBtn = new JButton("Generate Timetable");
        JButton viewBtn = new JButton("View Timetable");
        JButton logoutBtn = new JButton("Logout");

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.add(addFacultyBtn);
        panel.add(addSubjectBtn);
        panel.add(addRoomBtn);
        panel.add(generateBtn);
        panel.add(viewBtn);
        panel.add(logoutBtn);

        add(panel, BorderLayout.CENTER);

        // Actions
        addFacultyBtn.addActionListener(e -> new FacultyForm());
        addSubjectBtn.addActionListener(e -> new SubjectForm());
        addRoomBtn.addActionListener(e -> new RoomForm());

        generateBtn.addActionListener(e -> {
            manager.generateTimetable();
            JOptionPane.showMessageDialog(this, "✅ Timetable generated!");
        });

        viewBtn.addActionListener(e -> new TimetableView(manager));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });

        setVisible(true);
    }
}
