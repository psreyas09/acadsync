package utils;

import java.sql.*;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:acadsync.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

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

        conn.close();
    }

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
}
