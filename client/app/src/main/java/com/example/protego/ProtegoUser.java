package com.example.protego;

public class ProtegoUser {
    private String firstName;
    private String lastName;
    private String id;

    public String getName() {
        return this.firstName;
    }

    public void setName(String name) {
        this.lastName = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProtegoUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
