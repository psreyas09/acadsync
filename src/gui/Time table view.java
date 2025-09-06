package gui;

import core.TimetableManager;
import models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TimetableView extends JFrame {
    public TimetableView(TimetableManager manager) {
        setTitle("View Timetable");
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] cols = {"Subject", "Faculty", "Room", "Day", "Time"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        List<TimetableEntry> entries = manager.getEntries();
        for (TimetableEntry entry : entries) {
            String[] row = {
                entry.getSubject().getName(),
                entry.getFaculty().getName(),
                entry.getRoom().getRoomNo(),
                entry.getTimeSlot().getDay(),
                entry.getTimeSlot().getStartTime() + " - " + entry.getTimeSlot().getEndTime()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }
}