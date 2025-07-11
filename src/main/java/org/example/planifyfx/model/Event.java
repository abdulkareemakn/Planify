package org.example.planifyfx.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.example.planifyfx.util.EventInfo;

public abstract class Event {

    protected int id;
    protected String name;
    protected LocalDateTime time;
    protected Client client;
    protected int attendance;
    protected String eventType;


    public Event(String name, int attendance, Client client) {
        this.name = name;
        this.attendance = attendance;
        this.client = client;
    }

    public Event() {}



    public Event(EventInfo eventInfo) {
        this.name = eventInfo.getName();
        this.attendance = eventInfo.getAttendance();
        this.client = eventInfo.getClient();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract String toString();
}
