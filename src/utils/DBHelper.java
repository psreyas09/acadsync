package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.*;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:acadsync.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // ✅ Create all tables with proper resource management
    public static void createTables() throws SQLException {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Faculty table
            stmt.execute("CREATE TABLE IF NOT EXISTS Faculty (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "availableFrom TEXT," +
                    "availableTo TEXT)");

            // Subject table
            stmt.execute("CREATE TABLE IF NOT EXISTS Subject (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "hoursPerWeek INTEGER," +
                    "isOnline INTEGER)");

            // Room table
            stmt.execute("CREATE TABLE IF NOT EXISTS Room (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "roomNo TEXT," +
                    "capacity INTEGER)");

        // ClassGroup table
        stmt.execute("CREATE TABLE IF NOT EXISTS ClassGroup (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "department TEXT," +
                "semester INTEGER)");

        // TimeSlot table
        stmt.execute("CREATE TABLE IF NOT EXISTS TimeSlot (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "day TEXT," +
                "startTime TEXT," +
                "endTime TEXT)");

        // Admin table
        stmt.execute("CREATE TABLE IF NOT EXISTS Admin (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)");

        // FacultySubject table for many-to-many relationship
        stmt.execute("CREATE TABLE IF NOT EXISTS FacultySubject (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "facultyId INTEGER," +
                "subjectId INTEGER," +
                "FOREIGN KEY(facultyId) REFERENCES Faculty(id)," +
                "FOREIGN KEY(subjectId) REFERENCES Subject(id))");
        
        } // Close try-with-resources block
    }

    // ✅ Insert Methods
    public static void addFaculty(String name, String from, String to) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO Faculty (name, availableFrom, availableTo) VALUES (?, ?, ?)");
        pstmt.setString(1, name);
        pstmt.setString(2, from);
        pstmt.setString(3, to);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void addSubject(String name, int hoursPerWeek, boolean isOnline) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO Subject (name, hoursPerWeek, isOnline) VALUES (?, ?, ?)");
        pstmt.setString(1, name);
        pstmt.setInt(2, hoursPerWeek);
        pstmt.setInt(3, isOnline ? 1 : 0);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void addRoom(String roomNo, int capacity) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO Room (roomNo, capacity) VALUES (?, ?)");
        pstmt.setString(1, roomNo);
        pstmt.setInt(2, capacity);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void addClassGroup(String name, String department, int semester) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO ClassGroup (name, department, semester) VALUES (?, ?, ?)");
        pstmt.setString(1, name);
        pstmt.setString(2, department);
        pstmt.setInt(3, semester);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void addTimeSlot(String day, String start, String end) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO TimeSlot (day, startTime, endTime) VALUES (?, ?, ?)");
        pstmt.setString(1, day);
        pstmt.setString(2, start);
        pstmt.setString(3, end);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void addAdmin(String username, String password) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO Admin (username, password) VALUES (?, ?)");
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.executeUpdate();
        conn.close();
    }

    // ✅ Authentication
    public static boolean authenticateAdmin(String username, String password) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "SELECT * FROM Admin WHERE username=? AND password=?");
        pstmt.setString(1, username);
        pstmt.setString(2, password);

        ResultSet rs = pstmt.executeQuery();
        boolean valid = rs.next();
        conn.close();
        return valid;
    }

    // ✅ Fetch Methods
    public static List<Faculty> getAllFaculties() throws SQLException {
        List<Faculty> list = new ArrayList<>();
        Connection conn = connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Faculty");
        while (rs.next()) {
            Faculty faculty = new Faculty(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("availableFrom"),
                    rs.getString("availableTo")
            );
            // Load assigned subjects for this faculty
            faculty.setAssignedSubjects(getSubjectsForFaculty(faculty.getId()));
            list.add(faculty);
        }
        conn.close();
        return list;
    }

    public static List<Subject> getAllSubjects() throws SQLException {
        List<Subject> list = new ArrayList<>();
        Connection conn = connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Subject");
        while (rs.next()) {
            list.add(new Subject(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("hoursPerWeek"),
                    rs.getInt("isOnline") == 1
            ));
        }
        conn.close();
        return list;
    }

    public static List<Room> getAllRooms() throws SQLException {
        List<Room> list = new ArrayList<>();
        Connection conn = connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Room");
        while (rs.next()) {
            list.add(new Room(
                    rs.getInt("id"),
                    rs.getString("roomNo"),
                    rs.getInt("capacity")
            ));
        }
        conn.close();
        return list;
    }

    public static List<ClassGroup> getAllClassGroups() throws SQLException {
        List<ClassGroup> list = new ArrayList<>();
        Connection conn = connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM ClassGroup");
        while (rs.next()) {
            list.add(new ClassGroup(
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getInt("semester"),
                    new ArrayList<>() // subjects will be assigned later
            ));
        }
        conn.close();
        return list;
    }

    public static List<TimeSlot> getAllTimeSlots() throws SQLException {
        List<TimeSlot> list = new ArrayList<>();
        Connection conn = connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM TimeSlot");
        while (rs.next()) {
            list.add(new TimeSlot(
                    rs.getString("day"),
                    rs.getString("startTime"),
                    rs.getString("endTime")
            ));
        }
        conn.close();
        return list;
    }

    // ✅ Faculty-Subject relationship methods
    public static void assignSubjectToFaculty(int facultyId, int subjectId) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO FacultySubject (facultyId, subjectId) VALUES (?, ?)");
        pstmt.setInt(1, facultyId);
        pstmt.setInt(2, subjectId);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void removeSubjectFromFaculty(int facultyId, int subjectId) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM FacultySubject WHERE facultyId=? AND subjectId=?");
        pstmt.setInt(1, facultyId);
        pstmt.setInt(2, subjectId);
        pstmt.executeUpdate();
        conn.close();
    }

    public static List<Subject> getSubjectsForFaculty(int facultyId) throws SQLException {
        List<Subject> list = new ArrayList<>();
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "SELECT s.* FROM Subject s INNER JOIN FacultySubject fs ON s.id = fs.subjectId WHERE fs.facultyId = ?");
        pstmt.setInt(1, facultyId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            list.add(new Subject(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("hoursPerWeek"),
                    rs.getInt("isOnline") == 1
            ));
        }
        conn.close();
        return list;
    }

    public static int getLastInsertedFacultyId() throws SQLException {
        Connection conn = connect();
        ResultSet rs = conn.createStatement().executeQuery("SELECT last_insert_rowid() as id");
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        conn.close();
        return id;
    }

    // ✅ Delete Methods
    public static void deleteFaculty(int facultyId) throws SQLException {
        Connection conn = connect();
        
        // First remove all subject assignments for this faculty
        PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM FacultySubject WHERE facultyId = ?");
        pstmt1.setInt(1, facultyId);
        pstmt1.executeUpdate();
        
        // Then delete the faculty record
        PreparedStatement pstmt2 = conn.prepareStatement("DELETE FROM Faculty WHERE id = ?");
        pstmt2.setInt(1, facultyId);
        pstmt2.executeUpdate();
        
        conn.close();
    }

    public static void deleteSubject(int subjectId) throws SQLException {
        Connection conn = connect();
        
        // First remove all faculty assignments for this subject
        PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM FacultySubject WHERE subjectId = ?");
        pstmt1.setInt(1, subjectId);
        pstmt1.executeUpdate();
        
        // Then delete the subject record
        PreparedStatement pstmt2 = conn.prepareStatement("DELETE FROM Subject WHERE id = ?");
        pstmt2.setInt(1, subjectId);
        pstmt2.executeUpdate();
        
        conn.close();
    }

    public static void deleteRoom(int roomId) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Room WHERE id = ?");
        pstmt.setInt(1, roomId);
        pstmt.executeUpdate();
        conn.close();
    }

    // ✅ Update Methods
    public static void updateFaculty(int facultyId, String name, String availableFrom, String availableTo) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE Faculty SET name = ?, availableFrom = ?, availableTo = ? WHERE id = ?");
        pstmt.setString(1, name);
        pstmt.setString(2, availableFrom);
        pstmt.setString(3, availableTo);
        pstmt.setInt(4, facultyId);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void updateSubject(int subjectId, String name, int hoursPerWeek, boolean isOnline) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE Subject SET name = ?, hoursPerWeek = ?, isOnline = ? WHERE id = ?");
        pstmt.setString(1, name);
        pstmt.setInt(2, hoursPerWeek);
        pstmt.setInt(3, isOnline ? 1 : 0);
        pstmt.setInt(4, subjectId);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void updateRoom(int roomId, String roomNo, int capacity) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE Room SET roomNo = ?, capacity = ? WHERE id = ?");
        pstmt.setString(1, roomNo);
        pstmt.setInt(2, capacity);
        pstmt.setInt(3, roomId);
        pstmt.executeUpdate();
        conn.close();
    }
}

