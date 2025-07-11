package org.example.planifyfx.model;

import java.util.Random;
import java.util.Scanner;
import org.example.planifyfx.repository.EventRepository;
import org.example.planifyfx.util.EventInfo;

public class BirthdayEvent extends Event {
  private int age;
  private String theme;
  private int numberOfKids;

  public BirthdayEvent(EventInfo eventInfo, int age, String theme, int numberOfKids) {
    super(eventInfo);
    this.age = age;
    this.theme = theme;
    this.numberOfKids = numberOfKids;
    this.eventType = "Birthday";
  }

  public static BirthdayEvent promptForEventDetails() {
    Scanner scanner = new Scanner(System.in);

    EventInfo eventInfo = EventInfo.collectBasicInfo();

    System.out.println("What is the age of the person being celebrated?");
    int age = scanner.nextInt();
    scanner.nextLine();
    System.out.println("What is the theme of the event?");
    String theme = scanner.nextLine();
    System.out.println("How many kids are attending?");
    int numberOfKids = scanner.nextInt();
    scanner.nextLine();

    return new BirthdayEvent(eventInfo, age, theme, numberOfKids);
  }

  @Override
  public String toString() {
    return String.format("Name: %s, Attendance: %d, Client: %s, Event Type: Birthday Party",
        this.name, this.attendance, this.client.name);
  }

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
}
