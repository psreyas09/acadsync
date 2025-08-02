package core;

import models.*;

import java.util.ArrayList;
import java.util.List;

public class TimetableManager {
    private List<TimetableEntry> entries;

    public TimetableManager() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(TimetableEntry entry) {
        entries.add(entry);
    }

    public List<TimetableEntry> getEntries() {
        return entries;
    }

    public void generateTimetable() {
        // Placeholder: your timetable logic goes here
        System.out.println("Generating timetable...");
        // Future: assign subjects to faculty in available slots
    }

    public void detectClashes() {
        System.out.println("Checking for conflicts...");
        // Placeholder: clash detection logic
    }

    public void exportToPDF() {
        // Future enhancement: use iText or similar to export
        System.out.println("Exporting timetable to PDF... (not implemented)");
    }
}
