package core;

import java.util.*;
import models.*;

public class TimetableManager {
    private List<TimetableEntry> entries;

    public TimetableManager() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(TimetableEntry entry) {
        // check clash before adding
        if (!hasClash(entry)) {
            entries.add(entry);
            System.out.println("✅ Entry added: " + entry.getSubject().getName() +
                               " for " + entry.getClassGroup().getClassId() +
                               " at " + entry.getTimeSlot().getDay() + " " + entry.getTimeSlot().getStartTime());
        } else {
            System.out.println("❌ Clash detected! Could not add entry for " + entry.getSubject().getName());
        }
    }

    public List<TimetableEntry> getEntries() {
        return entries;
    }

    // ✅ Generate timetable with simple assignment
    public void generateTimetable(List<ClassGroup> classGroups,
                                  List<Subject> subjects,
                                  List<Faculty> faculties,
                                  List<Room> rooms,
                                  List<TimeSlot> slots) {

        System.out.println("⚡ Generating timetable...");

        Random rand = new Random();

        for (ClassGroup group : classGroups) {
            for (Subject subject : subjects) {
                // assign random faculty, room, and timeslot
                Faculty faculty = faculties.get(rand.nextInt(faculties.size()));
                Room room = rooms.get(rand.nextInt(rooms.size()));
                TimeSlot slot = slots.get(rand.nextInt(slots.size()));

                TimetableEntry entry = new TimetableEntry(faculty, group, subject, room, slot);
                addEntry(entry); // auto checks clashes
            }
        }

        System.out.println("✅ Timetable generation complete!");
    }

    // ✅ Clash detection
    private boolean hasClash(TimetableEntry newEntry) {
        for (TimetableEntry e : entries) {
            boolean sameDayTime = e.getTimeSlot().getDay().equals(newEntry.getTimeSlot().getDay()) &&
                                  e.getTimeSlot().getStartTime().equals(newEntry.getTimeSlot().getStartTime());

            if (sameDayTime) {
                // Clash if faculty, room, or classGroup already busy
                if (e.getFaculty().getId() == newEntry.getFaculty().getId() ||
                    e.getRoom().getId() == newEntry.getRoom().getId() ||
                    e.getClassGroup().getClassId().equals(newEntry.getClassGroup().getClassId())) {
                    return true;
                }
            }
        }
        return false;
    }

    // ✅ Manual clash check
    public void detectClashes() {
        System.out.println("🔎 Checking for conflicts...");
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                if (hasClash(entries.get(j))) {
                    System.out.println("⚠️ Clash detected between " +
                            entries.get(i).getSubject().getName() + " and " +
                            entries.get(j).getSubject().getName() +
                            " at " + entries.get(i).getTimeSlot().getDay() +
                            " " + entries.get(i).getTimeSlot().getStartTime());
                }
            }
        }
    }

    // ✅ Print timetable neatly
    public void printTimetable() {
        System.out.println("\n📅 Generated Timetable:");
        for (TimetableEntry e : entries) {
            System.out.println(e.getClassGroup().getClassId() + " | " +
                               e.getSubject().getName() + " | " +
                               e.getFaculty().getName() + " | " +
                               e.getRoom().getRoomNo() + " | " +
                               e.getTimeSlot().getDay() + " " +
                               e.getTimeSlot().getStartTime() + "-" +
                               e.getTimeSlot().getEndTime());
        }
    }

    // (Future) Export to PDF
    public void exportToPDF() {
        System.out.println("📄 Exporting timetable to PDF... (not implemented yet)");
    }
}
