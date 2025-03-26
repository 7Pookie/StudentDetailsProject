package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "technical_events")
public class TechnicalEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventID")
    private int eventID;

    @Column(nullable = false, unique = true)
    private String name;

    public TechnicalEvents() {}

    public TechnicalEvents(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object getEventID() {
        return eventID;
    }

    // Getters & Setters
}
