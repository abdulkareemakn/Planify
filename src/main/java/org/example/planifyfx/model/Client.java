package org.example.planifyfx.model;

import org.example.planifyfx.util.ContactInfo;

    public class Client {
        protected int id;
        protected String name;
        protected ContactInfo contactInfo;

        public Client(String name, ContactInfo contactInfo) {
            setName(name);
            setContactInfo(contactInfo);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmailAddress() {
            return contactInfo.getEmail();
        }

        public String getPhoneNumber() {
            return contactInfo.getPhoneNumber();
        }

        public void setContactInfo(ContactInfo contactInfo) {
            this.contactInfo = contactInfo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return null;
        }
    }
