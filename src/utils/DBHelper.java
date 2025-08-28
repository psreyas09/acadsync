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

    // ✅ Create all tables
    public static void createTables() throws SQLException {
        Connection conn = connect();
        Statement stmt = conn.createStatement();

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

        conn.close();
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
            list.add(new Faculty(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("availableFrom"),
                    rs.getString("availableTo")
            ));
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
}

