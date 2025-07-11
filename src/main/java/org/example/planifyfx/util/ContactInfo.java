package org.example.planifyfx.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ContactInfo {
    private String email;
    private String phoneNumber;

    public ContactInfo(String phoneNumber, String email) {
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,}$");
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        Pattern pattern = Pattern.compile("^\\+?\\d{10,15}$");
        if (!pattern.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        this.phoneNumber = phoneNumber;
    }

    public static ContactInfo promptForContactDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the email address: ");
        String email = scanner.nextLine();
        System.out.print("Enter the phone number: ");
        String phoneNumber = scanner.nextLine();
        scanner.close();
        return new ContactInfo(phoneNumber, email);
    }

    @Override
    public String toString() {
        return "ContactInfo{phoneNumber='" + phoneNumber + "', email='" + email + "'}";
    }
}