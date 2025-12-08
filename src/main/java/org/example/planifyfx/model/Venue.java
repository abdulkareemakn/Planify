package org.example.planifyfx.model;

/**
 * Represents a venue where events can be held.
 * Contains basic information like name, address, and capacity.
 */
public class Venue {
    
    private int id;
    private String name;
    private String address;
    private int capacity;

    /**
     * Creates a new Venue.
     * 
     * @param name Name of the venue
     * @param address Physical address
     * @param capacity Maximum number of people the venue can hold
     */
    public Venue(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    /**
     * Default constructor for loading from database.
     */
    public Venue() {}

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - Capacity: %d", name, address, capacity);
    }
}
