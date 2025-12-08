package org.example.planifyfx.model;

import java.time.LocalDateTime;

/**
 * Represents a birthday event with specific details like age, theme,
 * and number of kids attending.
 */
public class BirthdayEvent extends Event {
    
    private int age;
    private String theme;
    private int numberOfKids;

    /**
     * Creates a new Birthday Event.
     * 
     * @param name Event name
     * @param dateTime Date and time of the event
     * @param attendance Expected number of attendees
     * @param client The client booking the event
     * @param age Age of the person being celebrated
     * @param theme Party theme
     * @param numberOfKids Number of children attending
     */
    public BirthdayEvent(String name, LocalDateTime dateTime, int attendance, Client client,
                         int age, String theme, int numberOfKids) {
        super(name, dateTime, attendance, client);
        this.age = age;
        this.theme = theme;
        this.numberOfKids = numberOfKids;
        this.eventType = "Birthday";
    }

    /**
     * Default constructor for loading from database.
     */
    public BirthdayEvent() {
        this.eventType = "Birthday";
    }

    // Getters and Setters

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    @Override
    public String toString() {
        return String.format("Birthday Event: %s (Age: %d, Theme: %s)", name, age, theme);
    }
}
