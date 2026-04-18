package com.placement.model;

public class Admin {

    private long id;
    private String name;
    private String password;

    // Default constructor (needed for Gson JSON conversion)
    public Admin() {}

    // Constructor with all fields
    public Admin(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    // Getters
    public long getId()         { return id; }
    public String getName()     { return name; }
    public String getPassword() { return password; }

    // Setters
    public void setId(long id)           { this.id = id; }
    public void setName(String name)     { this.name = name; }
    public void setPassword(String pass) { this.password = pass; }
}