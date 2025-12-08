package org.example.planifyfx.model;

import java.time.LocalDateTime;

/**
 * Represents a wedding event with specific details like bride/groom names
 * and whether a photographer is required.
 */
public class WeddingEvent extends Event {
    
    private String brideName;
    private String groomName;
    private boolean photographerRequired;

    /**
     * Creates a new Wedding Event.
     * 
     * @param name Event name
     * @param dateTime Date and time of the event
     * @param attendance Expected number of attendees
     * @param client The client booking the event
     * @param brideName Name of the bride
     * @param groomName Name of the groom
     * @param photographerRequired Whether a photographer is needed
     */
    public WeddingEvent(String name, LocalDateTime dateTime, int attendance, Client client,
                        String brideName, String groomName, boolean photographerRequired) {
        super(name, dateTime, attendance, client);
        this.brideName = brideName;
        this.groomName = groomName;
        this.photographerRequired = photographerRequired;
        this.eventType = "Wedding";
    }

    /**
     * Default constructor for loading from database.
     */
    public WeddingEvent() {
        this.eventType = "Wedding";
    }

    // Getters and Setters

    public String getBrideName() {
        return brideName;
    }

    public void setBrideName(String brideName) {
        this.brideName = brideName;
    }

    public String getGroomName() {
        return groomName;
    }

    public void setGroomName(String groomName) {
        this.groomName = groomName;
    }

    public boolean isPhotographerRequired() {
        return photographerRequired;
    }

    public void setPhotographerRequired(boolean photographerRequired) {
        this.photographerRequired = photographerRequired;
    }

    @Override
    public String toString() {
        return String.format("Wedding Event: %s (Bride: %s, Groom: %s)", name, brideName, groomName);
    }
}
