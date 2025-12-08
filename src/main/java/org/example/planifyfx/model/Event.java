package org.example.planifyfx.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all event types.
 * Contains common fields shared by Wedding, Birthday, and Seminar events.
 */
public abstract class Event {

    protected int id;
    protected String name;
    protected LocalDateTime dateTime;
    protected int attendance;
    protected String eventType;
    protected Client client;
    protected Venue venue;

    /**
     * Constructor for creating a new event.
     */
    public Event(String name, LocalDateTime dateTime, int attendance, Client client) {
        this.name = name;
        this.dateTime = dateTime;
        this.attendance = attendance;
        this.client = client;
    }

    /**
     * Default constructor for loading events from database.
     */
    public Event() {}

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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the date and time formatted as "yyyy-MM-dd HH:mm".
     */
    public String getFormattedDateTime() {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getEventType() {
        return eventType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     * Abstract method - each event type must provide its own string representation.
     */
    public abstract String toString();
}
