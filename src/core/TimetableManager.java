package core;

import java.util.ArrayList;
import java.util.List;
import models.*;
import utils.DBHelper;

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
        entries.clear();

        try {
            List<Faculty> faculties = DBHelper.getAllFaculties();
            List<Subject> subjects = DBHelper.getAllSubjects();
            List<Room> rooms = DBHelper.getAllRooms();

            if (faculties.isEmpty() || subjects.isEmpty() || rooms.isEmpty()) {
                System.out.println("⚠ Not enough data to generate timetable.");
                return;
            }

            // Simple round-robin allocation
            for (int i = 0; i < subjects.size(); i++) {
                Faculty faculty = faculties.get(i % faculties.size());
                Room room = rooms.get(i % rooms.size());
                Subject subject = subjects.get(i);

                TimeSlot ts = new TimeSlot("Monday", "09:00", "10:00"); // demo slot
                TimetableEntry entry = new TimetableEntry(faculty, null, subject, room, ts);
                entries.add(entry);
            }

        } catch (Exception e) {
            System.out.println("Error generating timetable: " + e.getMessage());
        }
    }
}
