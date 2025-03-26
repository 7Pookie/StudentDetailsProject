package com.example.studentDetailsBackEnd.DTO;

import java.time.LocalDate;

public class TechnicalDetailRequest {
    private int studentID;
    private int eventID;
    private int eventCategoryID;
    private LocalDate eventDate;
    private String role;
    private String achievement;
    private String achievementDetails;
    private String otherDetails;

    // ✅ Getters & Setters
    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getEventCategoryID() {
        return eventCategoryID;
    }

    public void setEventCategoryID(int eventCategoryID) {
        this.eventCategoryID = eventCategoryID;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getAchievementDetails() {
        return achievementDetails;
    }

    public void setAchievementDetails(String achievementDetails) {
        this.achievementDetails = achievementDetails;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }
}
