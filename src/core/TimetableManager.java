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
        entries.clear(); // Clear existing entries before generating new ones

        try {
            List<Faculty> faculties = DBHelper.getAllFaculties();
            List<Subject> subjects = DBHelper.getAllSubjects();
            List<Room> rooms = DBHelper.getAllRooms();
            List<TimeSlot> timeSlots = DBHelper.getAllTimeSlots();

            // Shuffle rooms for better distribution
            java.util.Collections.shuffle(rooms);

            if (faculties.isEmpty() || subjects.isEmpty() || rooms.isEmpty()) {
                System.out.println("⚠ Not enough data to generate timetable.");
                return;
            }

            // Improved timetable generation with proper conflict checking and better distribution
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            String[] times = {"09:00", "10:00", "11:00", "14:00", "15:00"};
            
            System.out.println("🔄 Starting timetable generation...");
            System.out.println("Available: " + subjects.size() + " subjects, " + faculties.size() + " faculty, " + rooms.size() + " rooms");
            
            // Create a list of all possible time slots, shuffled for better distribution
            List<TimeSlot> availableSlots = new ArrayList<>();
            for (String day : days) {
                for (String startTime : times) {
                    String endTime = getEndTime(startTime);
                    availableSlots.add(new TimeSlot(day, startTime, endTime));
                }
            }
            
            // Shuffle the order to get better distribution across days
            java.util.Collections.shuffle(availableSlots);
            
            int currentSlotIndex = 0;
            int currentRoomIndex = 0; // Add room rotation
            
            for (Subject subject : subjects) {
                boolean scheduled = false;
                int attempts = 0;
                int maxAttempts = availableSlots.size() * faculties.size() * rooms.size();
                
                while (!scheduled && attempts < maxAttempts) {
                    // Get the current time slot (cycling through available slots)
                    TimeSlot timeSlot = availableSlots.get(currentSlotIndex % availableSlots.size());
                    
                    // Try to find a faculty assigned to this subject first
                    Faculty assignedFaculty = findAssignedFaculty(faculties, subject);
                    List<Faculty> facultiesToTry = new ArrayList<>();
                    
                    if (assignedFaculty != null) {
                        facultiesToTry.add(assignedFaculty);
                        // Add other faculties as backup
                        for (Faculty f : faculties) {
                            if (f.getId() != assignedFaculty.getId()) {
                                facultiesToTry.add(f);
                            }
                        }
                    } else {
                        facultiesToTry.addAll(faculties);
                    }
                    
                    // Try each faculty with rooms starting from current room index for better distribution
                    boolean slotTried = false;
                    for (Faculty faculty : facultiesToTry) {
                        // Try rooms starting from currentRoomIndex for better distribution
                        for (int i = 0; i < rooms.size(); i++) {
                            Room room = rooms.get((currentRoomIndex + i) % rooms.size());
                            if (!hasConflict(faculty, room, timeSlot)) {
                                TimetableEntry entry = new TimetableEntry(faculty, null, subject, room, timeSlot);
                                entries.add(entry);
                                scheduled = true;
                                System.out.println("✅ Scheduled " + subject.getName() + " on " + timeSlot.getDay() + " at " + timeSlot.getStartTime() + 
                                                 " with " + faculty.getName() + " in " + room.getRoomNo());
                                currentSlotIndex++; // Move to next slot for next subject
                                currentRoomIndex = (currentRoomIndex + 1) % rooms.size(); // Rotate room for next subject
                                break;
                            }
                            slotTried = true;
                        }
                        if (scheduled) break;
                    }
                    
                    if (!scheduled) {
                        if (slotTried) {
                            // This slot is full, try next slot
                            currentSlotIndex++;
                        }
                        attempts++;
                        
                        // Prevent infinite loops
                        if (attempts > 0 && attempts % (availableSlots.size() * 2) == 0) {
                            System.out.println("⚠ Struggling to place " + subject.getName() + " - trying different approach...");
                        }
                    }
                }
                
                if (!scheduled) {
                    System.out.println("⚠ Could not schedule " + subject.getName() + " - no available slots");
                }
            }

        } catch (Exception e) {
            System.out.println("Error generating timetable: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getEndTime(String startTime) {
        // Convert start time to end time (1 hour later)
        int hour = Integer.parseInt(startTime.split(":")[0]);
        return String.format("%02d:00", hour + 1);
    }

    private boolean hasConflict(Faculty faculty, Room room, TimeSlot timeSlot) {
        for (TimetableEntry entry : entries) {
            // Check faculty conflict - same faculty, same day, same time
            if (entry.getFaculty().getId() == faculty.getId() &&
                entry.getTimeSlot().getDay().equals(timeSlot.getDay()) &&
                entry.getTimeSlot().getStartTime().equals(timeSlot.getStartTime())) {
                return true;
            }
            // Check room conflict - same room, same day, same time
            if (entry.getRoom().getId() == room.getId() &&
                entry.getTimeSlot().getDay().equals(timeSlot.getDay()) &&
                entry.getTimeSlot().getStartTime().equals(timeSlot.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    private Faculty findAssignedFaculty(List<Faculty> faculties, Subject subject) {
        for (Faculty faculty : faculties) {
            if (faculty.getAssignedSubjects().contains(subject)) {
                return faculty;
            }
        }
        return null;
    }

    public List<TimetableEntry> getEntriesForRoom(int roomId) {
        List<TimetableEntry> roomEntries = new ArrayList<>();
        for (TimetableEntry entry : entries) {
            if (entry.getRoom().getId() == roomId) {
                roomEntries.add(entry);
            }
        }
        return roomEntries;
    }

    public List<TimetableEntry> getEntriesForFaculty(int facultyId) {
        List<TimetableEntry> facultyEntries = new ArrayList<>();
        for (TimetableEntry entry : entries) {
            if (entry.getFaculty().getId() == facultyId) {
                facultyEntries.add(entry);
            }
        }
        return facultyEntries;
    }

    public List<TimetableEntry> getEntriesForDay(String day) {
        List<TimetableEntry> dayEntries = new ArrayList<>();
        for (TimetableEntry entry : entries) {
            if (entry.getTimeSlot().getDay().equals(day)) {
                dayEntries.add(entry);
            }
        }
        return dayEntries;
    }
}
