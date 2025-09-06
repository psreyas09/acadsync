package gui;

import core.TimetableManager;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.*;

public class DayWiseScheduleView extends JFrame {
    private final TimetableManager manager;
    private JComboBox<String> dayComboBox;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private JButton refreshBtn, closeBtn;

    public DayWiseScheduleView(TimetableManager manager) {
        this.manager = manager;
        
        setTitle("Day-wise Schedule View");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
        setupLayout();
        loadDaySchedule();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeComponents() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        dayComboBox = new JComboBox<>(days);

        String[] columns = {"Time", "Subject", "Faculty", "Room", "Capacity", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scheduleTable = new JTable(tableModel);
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Time
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Subject
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Faculty
        scheduleTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Room
        scheduleTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Capacity
        scheduleTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Type

        refreshBtn = new JButton("Refresh");
        closeBtn = new JButton("Close");
        
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        // Top panel for day selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Select Day"));
        topPanel.add(new JLabel("Day: "));
        topPanel.add(dayComboBox);
        topPanel.add(refreshBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for schedule
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daily Schedule"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadDaySchedule() {
        tableModel.setRowCount(0);
        
        String selectedDay = (String) dayComboBox.getSelectedItem();
        if (selectedDay == null) {
            return;
        }

        List<TimetableEntry> dayEntries = manager.getEntriesForDay(selectedDay);
        
        // Sort by time
        dayEntries.sort((e1, e2) -> 
            e1.getTimeSlot().getStartTime().compareTo(e2.getTimeSlot().getStartTime()));

        for (TimetableEntry entry : dayEntries) {
            Object[] row = {
                entry.getTimeSlot().getStartTime() + " - " + entry.getTimeSlot().getEndTime(),
                entry.getSubject().getName(),
                entry.getFaculty().getName(),
                entry.getRoom().getRoomNo(),
                entry.getRoom().getCapacity(),
                entry.getSubject().isOnline() ? "Online" : "Offline"
            };
            tableModel.addRow(row);
        }
        
        // Update title with statistics
        setTitle("Day-wise Schedule - " + selectedDay + " (" + dayEntries.size() + " classes)");
    }

    private void setupEventHandlers() {
        dayComboBox.addActionListener(e -> loadDaySchedule());
        
        refreshBtn.addActionListener(e -> {
            manager.generateTimetable(); // Regenerate timetable
            loadDaySchedule(); // Refresh display
            JOptionPane.showMessageDialog(this, "✅ Schedule refreshed!");
        });
        
        closeBtn.addActionListener(e -> dispose());
    }
}
