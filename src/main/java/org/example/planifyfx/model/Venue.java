package org.example.planifyfx.model;


import java.util.Scanner;

public class Venue {
    protected String name;
    protected String address;
    protected int capacity;
    protected double basePrice;
    protected double squareFeet;
    protected boolean isOutdoor;
    protected boolean hasRestrooms;

    public Venue(String name, String address, int capacity, double basePrice, double squareFeet,
                 boolean isOutdoor, boolean hasRestrooms) {
        setName(name);
        setAddress(address);
        setCapacity(capacity);
        setBasePrice(basePrice);
        setSquareFeet(squareFeet);
        setOutdoor(isOutdoor);
        setHasRestrooms(hasRestrooms);
    }

    public static Venue promptForDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the venue: ");
        String name = scanner.nextLine();
        System.out.print("Enter the address of the venue: ");
        String address = scanner.nextLine();
        System.out.print("Enter the capacity of the venue: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the base price of the venue: ");
        double basePrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter the square feet of the venue: ");
        double squareFeet = scanner.nextDouble();
        scanner.nextLine();
        boolean isOutdoor;
        boolean hasRestrooms;

        String temp;

        do {
            System.out.print("Is the venue outdoor? (true/false)");
            temp = scanner.nextLine().trim().toLowerCase();
        } while (!temp.equals("true") && !temp.equals("false"));
        isOutdoor = Boolean.parseBoolean(temp);

        do {
            System.out.print("Does the venue have restrooms? (true/false)");
            temp = scanner.nextLine().trim().toLowerCase();
        } while (!temp.equals("true") && !temp.equals("false"));
        hasRestrooms = Boolean.parseBoolean(temp);

        return new Venue(name, address, capacity, basePrice, squareFeet, isOutdoor, hasRestrooms);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(double squareFeet) {
        this.squareFeet = squareFeet;
    }

    public boolean isOutdoor() {
        return isOutdoor;
    }

    public void setOutdoor(boolean outdoor) {
        isOutdoor = outdoor;
    }

    public boolean isHasRestrooms() {
        return hasRestrooms;
    }

    public void setHasRestrooms(boolean hasRestrooms) {
        this.hasRestrooms = hasRestrooms;
    }
}

