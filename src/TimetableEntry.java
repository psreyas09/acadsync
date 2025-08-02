package models;

public class TimetableEntry {
    private Faculty faculty;
    private ClassGroup classGroup;
    private Subject subject;
    private Room room;
    private TimeSlot timeSlot;

    public TimetableEntry(Faculty faculty, ClassGroup classGroup, Subject subject, Room room, TimeSlot timeSlot) {
        this.faculty = faculty;
        this.classGroup = classGroup;
        this.subject = subject;
        this.room = room;
        this.timeSlot = timeSlot;
    }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public ClassGroup getClassGroup() { return classGroup; }
    public void setClassGroup(ClassGroup classGroup) { this.classGroup = classGroup; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public TimeSlot getTimeSlot() { return timeSlot; }
    public void setTimeSlot(TimeSlot timeSlot) { this.timeSlot = timeSlot; }
}