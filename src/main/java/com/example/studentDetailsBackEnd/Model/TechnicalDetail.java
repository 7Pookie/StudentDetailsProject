package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "technical_event_details")
public class TechnicalDetail {

    @Id
    @Column(name = "technical_event_details_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "studentID",referencedColumnName = "studentID", foreignKey = @ForeignKey(name = "technical_studentID")) 
    private Student student;

    @ManyToOne
    @JoinColumn(name = "eventID",referencedColumnName = "eventID", foreignKey = @ForeignKey(name = "technical_eventID"))
    private TechnicalEvents technicalEvent;

    @ManyToOne
    @JoinColumn(name = "eventCategoryID", referencedColumnName = "event_categoryid", foreignKey = @ForeignKey(name = "technical_categoryID"))
    private EventCategory eventCategory;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String achievement;  // "1st", "2nd", "3rd", "Consolation"

    @Column(nullable = true, columnDefinition = "TEXT")
    private String achievementDetails;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String otherDetails;

    @Column(nullable = false)
    private String status = "PENDING";

    public TechnicalDetail() {}

    public TechnicalDetail(Student student,TechnicalEvents technicalEvent, EventCategory eventCategory, LocalDate eventDate, String role, String achievement, String achievementDetails, String otherDetails) {
        this.student = student;
        this.technicalEvent = technicalEvent;
        this.eventCategory = eventCategory;
        this.eventDate = eventDate;
        this.role = role;
        this.achievement = achievement;
        this.achievementDetails = achievementDetails;
        this.otherDetails = otherDetails;
        this.status = "PENDING";
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getStudentID() {
       return (int) student.getStudentID();
    }

    public int getTechnicalEventDetailsId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public TechnicalEvents getEvent() {
        return technicalEvent;  
    }

    public void setEvent(TechnicalEvents event) {
        this.technicalEvent = event;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
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

    // Getters & Setters
}
