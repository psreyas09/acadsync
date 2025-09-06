package gui;

import core.TimetableManager;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.*;
import utils.DBHelper;

public class RoomTimetableView extends JFrame {
    private TimetableManager manager;
    private JComboBox<Room> roomComboBox;
    private JTable timetableTable;
    private DefaultTableModel tableModel;
    private JButton refreshBtn, closeBtn, viewAllRoomsBtn, dayWiseBtn;

    public RoomTimetableView(TimetableManager manager) {
        this.manager = manager;
        
        setTitle("Room Timetable View");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
        setupLayout();
        loadRooms();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeComponents() {
        roomComboBox = new JComboBox<>();
        roomComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Room) {
                    Room room = (Room) value;
                    setText(room.getRoomNo() + " (Capacity: " + room.getCapacity() + ")");
                }
                return this;
            }
        });

        String[] columns = {"Day", "Time", "Subject", "Faculty", "Hours/Week", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        timetableTable = new JTable(tableModel);
        timetableTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Day
        timetableTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Time
        timetableTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Subject
        timetableTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Faculty
        timetableTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Hours
        timetableTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Type

        refreshBtn = new JButton("Refresh");
        closeBtn = new JButton("Close");
        viewAllRoomsBtn = new JButton("View All Rooms Schedule");
        dayWiseBtn = new JButton("Day-wise Schedule");
        
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        viewAllRoomsBtn.setBackground(new Color(60, 179, 113));
        viewAllRoomsBtn.setForeground(Color.WHITE);
        dayWiseBtn.setBackground(new Color(255, 140, 0));
        dayWiseBtn.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        // Top panel for room selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Select Room"));
        topPanel.add(new JLabel("Room: "));
        topPanel.add(roomComboBox);
        topPanel.add(refreshBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for timetable
        JScrollPane scrollPane = new JScrollPane(timetableTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Room Schedule"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(viewAllRoomsBtn);
        bottomPanel.add(dayWiseBtn);
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadRooms() {
        try {
            roomComboBox.removeAllItems();
            List<Room> rooms = DBHelper.getAllRooms();
            for (Room room : rooms) {
                roomComboBox.addItem(room);
            }
            if (!rooms.isEmpty()) {
                loadRoomTimetable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading rooms: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRoomTimetable() {
        tableModel.setRowCount(0);
        
        Room selectedRoom = (Room) roomComboBox.getSelectedItem();
        if (selectedRoom == null) {
            return;
        }

        List<TimetableEntry> roomEntries = manager.getEntriesForRoom(selectedRoom.getId());
        
        // Sort by day and time for better display
        roomEntries.sort((e1, e2) -> {
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            int day1 = java.util.Arrays.asList(days).indexOf(e1.getTimeSlot().getDay());
            int day2 = java.util.Arrays.asList(days).indexOf(e2.getTimeSlot().getDay());
            
            if (day1 != day2) {
                return Integer.compare(day1, day2);
            }
            return e1.getTimeSlot().getStartTime().compareTo(e2.getTimeSlot().getStartTime());
        });

        for (TimetableEntry entry : roomEntries) {
            Object[] row = {
                entry.getTimeSlot().getDay(),
                entry.getTimeSlot().getStartTime() + " - " + entry.getTimeSlot().getEndTime(),
                entry.getSubject().getName(),
                entry.getFaculty().getName(),
                entry.getSubject().getHoursPerWeek() + "h",
                entry.getSubject().isOnline() ? "Online" : "Offline"
            };
            tableModel.addRow(row);
        }
        
        // Update title to show selected room
        setTitle("Room Timetable - " + selectedRoom.getRoomNo() + " (" + roomEntries.size() + " classes)");
    }

    private void setupEventHandlers() {
        roomComboBox.addActionListener(e -> loadRoomTimetable());
        
        refreshBtn.addActionListener(e -> {
            manager.generateTimetable(); // Regenerate timetable
            loadRoomTimetable(); // Refresh display
            JOptionPane.showMessageDialog(this, "✅ Timetable refreshed!");
        });
        
        closeBtn.addActionListener(e -> dispose());
        
        viewAllRoomsBtn.addActionListener(e -> new AllRoomsScheduleView(manager));
        
        dayWiseBtn.addActionListener(e -> new DayWiseScheduleView(manager));
    }
}
