package com.example.safeguard;
public class Contact {
    private String name;
    private String email;
    private String password;
    private String contact1;
    private String contact2;

    // Constructor
    public Contact(String name, String email, String password, String contact1, String contact2) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact1 = contact1;
        this.contact2 = contact2;
    }

    // Getters and setters for name, email, password, contact1, and contact2
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }
}
