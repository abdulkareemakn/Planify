package org.example.planifyfx.model;

import java.util.Random;
import java.util.Scanner;

import org.example.planifyfx.util.EventInfo;

public class WeddingEvent extends Event {
    private String brideName;
    private String groomName;
    private boolean photographerRequired;

    public WeddingEvent(
            EventInfo eventInfo, String brideName, String groomName, boolean photographerRequired) {
        super(eventInfo);
        setBrideName(brideName);
        setGroomName(groomName);
        setPhotographerRequired(photographerRequired);
        this.eventType = "Wedding";
    }

    public static WeddingEvent promptForEventDetails() {
        Scanner scanner = new Scanner(System.in);

        EventInfo eventInfo = EventInfo.collectBasicInfo();

        System.out.print("What is the name of the bride?");
        String brideName = scanner.nextLine();
        System.out.print("What is the name of the groom?");
        String groomName = scanner.nextLine();

        boolean photographerRequired;
        String temp;

        do {
            System.out.print("Is a photographer required? (true/false)");
            temp = scanner.nextLine().trim().toLowerCase();
        } while (!temp.equals("true") && !temp.equals("false"));

        photographerRequired = Boolean.parseBoolean(temp);

        return new WeddingEvent(eventInfo, brideName, groomName, photographerRequired);
    }

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
        return String.format("Name: %s, Attendance: %d, Client: %s, Event Type: Wedding", this.name,
                this.attendance, this.client.name);
    }
}
