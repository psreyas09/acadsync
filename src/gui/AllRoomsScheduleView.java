package gui;

import core.TimetableManager;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.*;
import utils.DBHelper;

public class AllRoomsScheduleView extends JFrame {
    private final TimetableManager manager;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private JButton refreshBtn, closeBtn;

    public AllRoomsScheduleView(TimetableManager manager) {
        this.manager = manager;
        
        setTitle("All Rooms Schedule");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
        setupLayout();
        loadAllRoomsSchedule();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeComponents() {
        String[] columns = {"Room", "Day", "Time", "Subject", "Faculty", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scheduleTable = new JTable(tableModel);
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Room
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Day
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Time
        scheduleTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Subject
        scheduleTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Faculty
        scheduleTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Type

        refreshBtn = new JButton("Refresh Schedule");
        closeBtn = new JButton("Close");
        
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        // Top panel with info
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("📅 Complete Room Utilization Schedule"));
        add(topPanel, BorderLayout.NORTH);

        // Center panel for schedule table
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Rooms Weekly Schedule"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(refreshBtn);
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadAllRoomsSchedule() {
        tableModel.setRowCount(0);
        
        List<TimetableEntry> allEntries = manager.getEntries();
        
        // Sort by room, then day, then time
        allEntries.sort((e1, e2) -> {
            // First sort by room number
            int roomCompare = e1.getRoom().getRoomNo().compareTo(e2.getRoom().getRoomNo());
            if (roomCompare != 0) {
                return roomCompare;
            }
            
            // Then by day
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            int day1 = java.util.Arrays.asList(days).indexOf(e1.getTimeSlot().getDay());
            int day2 = java.util.Arrays.asList(days).indexOf(e2.getTimeSlot().getDay());
            
            if (day1 != day2) {
                return Integer.compare(day1, day2);
            }
            
            // Finally by time
            return e1.getTimeSlot().getStartTime().compareTo(e2.getTimeSlot().getStartTime());
        });

        for (TimetableEntry entry : allEntries) {
            Object[] row = {
                entry.getRoom().getRoomNo(),
                entry.getTimeSlot().getDay(),
                entry.getTimeSlot().getStartTime() + " - " + entry.getTimeSlot().getEndTime(),
                entry.getSubject().getName(),
                entry.getFaculty().getName(),
                entry.getSubject().isOnline() ? "Online" : "Offline"
            };
            tableModel.addRow(row);
        }
        
        // Update title with statistics
        try {
            List<Room> allRooms = DBHelper.getAllRooms();
            setTitle("All Rooms Schedule - " + allRooms.size() + " rooms, " + allEntries.size() + " scheduled classes");
        } catch (Exception ex) {
            setTitle("All Rooms Schedule - " + allEntries.size() + " scheduled classes");
        }
    }

    private void setupEventHandlers() {
        refreshBtn.addActionListener(e -> {
            manager.generateTimetable(); // Regenerate timetable
            loadAllRoomsSchedule(); // Refresh display
            JOptionPane.showMessageDialog(this, "✅ Schedule refreshed for all rooms!");
        });
        
        closeBtn.addActionListener(e -> dispose());
    }
}
