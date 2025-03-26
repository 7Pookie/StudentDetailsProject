package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "event_category")
public class EventCategory {

    @Id
    @Column(name = "event_categoryid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventCategoryID;

    @Column(name = "event_category_name",nullable = false, unique = true)
    private String eventCategoryName;

    public EventCategory() {}

    public EventCategory(String category) {
        this.eventCategoryName = category;
    }

    public String getEventCategoryName() {
        return eventCategoryName;
    }

    public Object getEventCategoryID() {
       return eventCategoryID;
    }

    // Getters & Setters
}
