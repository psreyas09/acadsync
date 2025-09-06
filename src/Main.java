import gui.LoginGUI;
import utils.DBHelper;

public class Main {
    public static void main(String[] args) {
        try {
            // Ensure DB & tables exist
            DBHelper.createTables();
        } catch (Exception e) {
            System.out.println("⚠ Database error: " + e.getMessage());
        }

        // Start GUI Login
        new LoginGUI();
    }
}
