package org.example.planifyfx.model;

/**
 * Represents a client who books events.
 * Contains contact information like name, email, and phone number.
 */
public class Client {
    
    private int id;
    private String name;
    private String email;
    private String phoneNumber;

    /**
     * Creates a new Client.
     * 
     * @param name Client's full name
     * @param email Client's email address
     * @param phoneNumber Client's phone number
     */
    public Client(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Default constructor for loading from database.
     */
    public Client() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, email);
    }
}
