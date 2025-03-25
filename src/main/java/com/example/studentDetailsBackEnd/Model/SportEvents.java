package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "sport_events")
public class SportEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventID")
    private int eventID;

    @Column(name= "sport_event_name", nullable = false, unique = true)
    private String sportEventName;

    public SportEvents() {}

    public SportEvents(String sportEventName) {
        this.sportEventName = sportEventName;
    }

    public String getSportEventName() {
        return sportEventName;
    }

    public void setSportEventName(String sportEventName) {
        this.sportEventName = sportEventName;
    }

    public Object getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
