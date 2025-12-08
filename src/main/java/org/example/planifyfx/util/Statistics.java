package org.example.planifyfx.util;

import org.example.planifyfx.repository.EventRepository;

/**
 * Utility class for retrieving event statistics.
 * All statistics are calculated live from the database.
 */
public class Statistics {

    /**
     * Gets the total number of events in the system.
     */
    public static int getTotalEvents() {
        return EventRepository.getTotalEventsCount();
    }

    /**
     * Gets the number of wedding events.
     */
    public static int getTotalWeddingEvents() {
        return EventRepository.getWeddingEventsCount();
    }

    /**
     * Gets the number of birthday events.
     */
    public static int getTotalBirthdayEvents() {
        return EventRepository.getBirthdayEventsCount();
    }

    /**
     * Gets the number of seminar events.
     */
    public static int getTotalSeminarEvents() {
        return EventRepository.getSeminarEventsCount();
    }
}
