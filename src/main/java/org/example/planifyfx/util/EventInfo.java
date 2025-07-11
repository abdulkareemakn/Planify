package org.example.planifyfx.util;

import java.util.Scanner;

import org.example.planifyfx.Planify;
import org.example.planifyfx.model.Client;
import org.example.planifyfx.repository.ClientRepository;


public class EventInfo {
    protected String name;
    protected int attendance;
    protected Client client;

    public EventInfo(String name, int attendance, Client client) {
        setName(name);
        setAttendance(attendance);
        setClient(client);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static EventInfo collectBasicInfo() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("What is the name of the client? ");
        String clientName = scanner.nextLine();

        Client client = new Client(clientName, ContactInfo.promptForContactDetails());

        System.out.print("What is the name of the event? ");
        String name = scanner.nextLine();
        System.out.print("How many people are attending? ");
        int attendance = scanner.nextInt();
        scanner.nextLine();

        return new EventInfo(name, attendance, client);
    }
}
