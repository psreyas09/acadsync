import java.sql.*;

public class DBSetup {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:acadsync.db");
            Statement stmt = conn.createStatement();

            // Faculty Table
            stmt.execute("CREATE TABLE IF NOT EXISTS Faculty (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL," +
                         "availableFrom TEXT," +
                         "availableTo TEXT)");

            // Subject Table
            stmt.execute("CREATE TABLE IF NOT EXISTS Subject (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL," +
                         "hoursPerWeek INTEGER)");

            // Room Table
            stmt.execute("CREATE TABLE IF NOT EXISTS Room (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "roomNo TEXT," +
                         "capacity INTEGER)");

            System.out.println("Tables created successfully ✅");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
