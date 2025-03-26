package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "sport_details")
public class SportDetail {

    @Id
    @Column(name = "sport_details_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "studentID",referencedColumnName = "studentID", foreignKey = @ForeignKey(name = "sport_studentID")) 
    private Student student;

    @ManyToOne
    @JoinColumn(name = "eventID",referencedColumnName = "eventID", foreignKey = @ForeignKey(name = "sport_eventID"))
    private SportEvents sportEvent;

    @ManyToOne
    @JoinColumn(name = "sportCategoryID", referencedColumnName = "sport_categoryid", foreignKey = @ForeignKey(name = "sport_categoryID"))
    private SportEventCategory sportEventCategory;

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

    @Lob
    @Column(columnDefinition = "LONGBLOB", name="offer_letter")
    private byte[] offerLetter;

    public byte[] getOfferLetter() {
        return offerLetter;
    }

    public void setOfferLetter(byte[] offerLetter) {
        this.offerLetter = offerLetter;
    }

    public SportDetail() {}

    public SportDetail(Student student,SportEvents sportEvent, SportEventCategory sportEventCategory, LocalDate eventDate, String role, String achievement, String achievementDetails, String otherDetails) {
        this.student = student;
        this.sportEvent = sportEvent;
        this.sportEventCategory = sportEventCategory;
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

    public int getSportEventDetailsId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SportEvents getEvent() {
        return sportEvent;  
    }

    public void setEvent(SportEvents event) {
        this.sportEvent = event;
    }

    public SportEventCategory getEventCategory() {
        return sportEventCategory;
    }

    public void setEventCategory(SportEventCategory sportEventCategory) {
        this.sportEventCategory = sportEventCategory;
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

    public int getSportDetailID() {
        return id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
