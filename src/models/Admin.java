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
    }
}
