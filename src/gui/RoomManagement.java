package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Room;
import utils.DBHelper;

public class RoomManagement extends JFrame {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, deleteBtn, refreshBtn, closeBtn;

    public RoomManagement() {
        setTitle("Room Management");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadRoomData();

        setVisible(true);
    }

    private void initializeComponents() {
        // Create table with columns
        String[] columns = {"ID", "Room Number", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Room Number
        roomTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Capacity

        // Create buttons
        addBtn = new JButton("Add Room");
        deleteBtn = new JButton("Delete Room");
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
        JScrollPane scrollPane = new JScrollPane(roomTable);
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
        infoPanel.add(new JLabel("Room Management - Select a room to delete"));
        add(infoPanel, BorderLayout.NORTH);
    }

    private void setupEventHandlers() {
        addBtn.addActionListener(e -> {
            new RoomForm();
            loadRoomData(); // Refresh after adding
        });

        deleteBtn.addActionListener(e -> deleteSelectedRoom());

        refreshBtn.addActionListener(e -> loadRoomData());

        closeBtn.addActionListener(e -> dispose());

        // Enable/disable delete button based on selection
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = roomTable.getSelectedRow() != -1;
            deleteBtn.setEnabled(hasSelection);
        });
    }

    private void loadRoomData() {
        tableModel.setRowCount(0); // Clear existing data
        
        try {
            List<Room> rooms = DBHelper.getAllRooms();
            for (Room room : rooms) {
                Object[] row = {
                    room.getId(),
                    room.getRoomNo(),
                    room.getCapacity()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading room data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete.");
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 1);
        int roomId = (int) tableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete room '" + roomNumber + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                DBHelper.deleteRoom(roomId);
                JOptionPane.showMessageDialog(this, "✅ Room deleted successfully!");
                loadRoomData(); // Refresh the table
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠ Error deleting room: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
