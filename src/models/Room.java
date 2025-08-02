package models;

public class Room {
    private int id;
    private String roomNo;
    private int capacity;

    public Room(int id, String roomNo, int capacity) {
        this.id = id;
        this.roomNo = roomNo;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}