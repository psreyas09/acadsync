package models;

public class Subject {
    private int id;
    private String name;
    private int hoursPerWeek;
    private boolean isOnline;

    public Subject(int id, String name, int hoursPerWeek, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.hoursPerWeek = hoursPerWeek;
        this.isOnline = isOnline;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHoursPerWeek() { return hoursPerWeek; }
    public void setHoursPerWeek(int hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
}
