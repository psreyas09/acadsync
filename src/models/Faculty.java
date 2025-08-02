package models;

public class Faculty {
    private int id;
    private String name;
    private String availableFrom;
    private String availableTo;

    public Faculty(int id, String name, String availableFrom, String availableTo) {
        this.id = id;
        this.name = name;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvailableFrom() { return availableFrom; }
    public void setAvailableFrom(String availableFrom) { this.availableFrom = availableFrom; }

    public String getAvailableTo() { return availableTo; }
    public void setAvailableTo(String availableTo) { this.availableTo = availableTo; }
}