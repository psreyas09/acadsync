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

    @Override
    public String toString() {
        return name + " (" + hoursPerWeek + "h/week" + (isOnline ? ", Online" : "") + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subject subject = (Subject) obj;
        return id == subject.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
