package org.example.planifyfx.model;

import org.example.planifyfx.util.EventInfo;

public class SeminarEvent extends Event{
    private String chiefGuest;
    private String speaker;
    private String topic;

    public SeminarEvent(EventInfo eventInfo, String chiefGuest, String speaker, String topic) {
        super(eventInfo);
        this.chiefGuest = chiefGuest;
        this.speaker = speaker;
        this.topic = topic;
        this.eventType = "Seminar";
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Attendance: %d, Client: %s, Event Type: Seminar", this.name, this.attendance, this.client.name);
    }

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
}
