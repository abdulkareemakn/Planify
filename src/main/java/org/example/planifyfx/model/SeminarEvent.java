package org.example.planifyfx.model;

import java.time.LocalDateTime;

/**
 * Represents a seminar event with specific details like chief guest,
 * speaker, and topic.
 */
public class SeminarEvent extends Event {
    
    private String chiefGuest;
    private String speaker;
    private String topic;

    /**
     * Creates a new Seminar Event.
     * 
     * @param name Event name
     * @param dateTime Date and time of the event
     * @param attendance Expected number of attendees
     * @param client The client booking the event
     * @param chiefGuest Name of the chief guest
     * @param speaker Name of the speaker
     * @param topic Topic of the seminar
     */
    public SeminarEvent(String name, LocalDateTime dateTime, int attendance, Client client,
                        String chiefGuest, String speaker, String topic) {
        super(name, dateTime, attendance, client);
        this.chiefGuest = chiefGuest;
        this.speaker = speaker;
        this.topic = topic;
        this.eventType = "Seminar";
    }

    /**
     * Default constructor for loading from database.
     */
    public SeminarEvent() {
        this.eventType = "Seminar";
    }

    // Getters and Setters

    public String getChiefGuest() {
        return chiefGuest;
    }

    public void setChiefGuest(String chiefGuest) {
        this.chiefGuest = chiefGuest;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return String.format("Seminar Event: %s (Topic: %s, Speaker: %s)", name, topic, speaker);
    }
}
