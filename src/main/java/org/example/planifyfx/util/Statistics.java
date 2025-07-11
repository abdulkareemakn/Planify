package org.example.planifyfx.util;

import org.example.planifyfx.model.Event;
import org.example.planifyfx.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

public class Statistics {

    public static int totalEvents = EventRepository.getTotalEventsCount();

    public static int totalWeddingEvents = EventRepository.getWeddingEventsCount();

    public static int totalBirthdayEvents = EventRepository.getBirthdayEventsCount();

    public static int totalSeminarEvents = EventRepository.getSeminarEventsCount();

    public static int eventsAdded = 0;


}

