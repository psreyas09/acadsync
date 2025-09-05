<<<<<<< HEAD
 import core.TimetableManager;
import models.*;
import utils.DBHelper;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // ✅ Step 1: Setup DB
            DBHelper.createTables();
            System.out.println("✅ Tables ready!");

            // ✅ Step 2: Insert sample Admin (only first time, then comment out)
            try {
                DBHelper.addAdmin("admin", "1234");
                System.out.println("✅ Sample admin created (username: admin, password: 1234)");
            } catch (Exception e) {
                System.out.println("ℹ️ Admin already exists.");
            }

            // ✅ Step 3: Authenticate Admin
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter admin username: ");
            String user = sc.nextLine();
            System.out.print("Enter password: ");
            String pass = sc.nextLine();

            if (!DBHelper.authenticateAdmin(user, pass)) {
                System.out.println("❌ Invalid credentials. Exiting...");
                return;
            }
            System.out.println("✅ Admin login successful!");

            // ✅ Step 4: Insert sample Faculty/Subjects/Rooms/Classes/Slots (only once)
            try {
                DBHelper.addFaculty("Dr. Smith", "09:00", "17:00");
                DBHelper.addFaculty("Prof. Alice", "10:00", "16:00");

                DBHelper.addSubject("Mathematics", 4, false);
                DBHelper.addSubject("Computer Science", 5, true);

                DBHelper.addRoom("A101", 60);
                DBHelper.addRoom("B202", 45);

                DBHelper.addClassGroup("CS1", "CSE", 3);
                DBHelper.addClassGroup("EE1", "EEE", 3);

                DBHelper.addTimeSlot("Monday", "09:00", "10:00");
                DBHelper.addTimeSlot("Monday", "10:00", "11:00");
                DBHelper.addTimeSlot("Tuesday", "09:00", "10:00");

                System.out.println("✅ Sample data inserted!");
            } catch (Exception e) {
                System.out.println("ℹ️ Sample data already exists.");
            }

            // ✅ Step 5: Fetch data
            List<Faculty> faculties = DBHelper.getAllFaculties();
            List<Subject> subjects = DBHelper.getAllSubjects();
            List<Room> rooms = DBHelper.getAllRooms();
            List<ClassGroup> classGroups = DBHelper.getAllClassGroups();
            List<TimeSlot> slots = DBHelper.getAllTimeSlots();

            // ✅ Step 6: Generate Timetable
            TimetableManager manager = new TimetableManager();
            manager.generateTimetable(classGroups, subjects, faculties, rooms, slots);

            // ✅ Step 7: Print timetable
            manager.printTimetable();

            // ✅ Step 8: Detect clashes
            manager.detectClashes();

        } catch (Exception e) {
            e.printStackTrace();
        }
=======
package models;

public class Admin {
    private int id;
    private String username;
    private String password; // ⚠️ stored as plain text for now (later can hash)

    public Admin(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Admin(String username, String password) {
        this(-1, username, password); // -1 when id not yet assigned
    }

    // ✅ Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // ✅ Simple validation
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
>>>>>>> ddd249eb9fd096c3d858c06faa48b5e9fe9ed862
    }
}
