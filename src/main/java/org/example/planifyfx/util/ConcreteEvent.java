package org.example.planifyfx.util;
import org.example.planifyfx.model.Event;

import java.time.LocalDateTime;

public class ConcreteEvent extends Event {

    protected String venueName;
    protected String clientName;

    public ConcreteEvent(String name, String eventType, LocalDateTime time, String venueName, int attendance,
                            String clientName) {
        super();
        this.name = name;
        this.eventType = eventType;
        this.time = time;
        this.venueName = venueName;
        this.attendance = attendance;
        this.clientName = clientName;

    }

    @Override
    public String toString() {return null;}
}
