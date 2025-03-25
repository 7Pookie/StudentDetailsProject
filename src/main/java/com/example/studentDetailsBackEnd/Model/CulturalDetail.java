package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cultural_event_details")
public class CulturalDetail {

    @Id
    @Column(name = "cultural_event_details_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "studentID",referencedColumnName = "studentID", foreignKey = @ForeignKey(name = "cultural_studentID")) 
    private Student student;

    @ManyToOne
    @JoinColumn(name = "eventID",referencedColumnName = "eventID", foreignKey = @ForeignKey(name = "cultural_eventID"))
    private CulturalEvents culturalEvent;

    @ManyToOne
    @JoinColumn(name = "eventCategoryID", referencedColumnName = "event_categoryid", foreignKey = @ForeignKey(name = "cultural_categoryID"))
    private EventCategory eventCategory;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String achievement;  

    @Column(nullable = true, columnDefinition = "TEXT")
    private String achievementDetails;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String otherDetails;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(nullable = true)
    private String remark = "";

    public CulturalDetail() {}

    public CulturalDetail(Student student,CulturalEvents culturalEvent, EventCategory eventCategory, LocalDate eventDate, String role, String achievement, String achievementDetails, String otherDetails) {
        this.student = student;
        this.culturalEvent = culturalEvent;
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

    public int getCulturalEventDetailsId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public CulturalEvents getEvent() {
        return culturalEvent;  
    }

    public void setEvent(CulturalEvents event) {
        this.culturalEvent = event;
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

    public int getCulturalDetailID() {
        return id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
