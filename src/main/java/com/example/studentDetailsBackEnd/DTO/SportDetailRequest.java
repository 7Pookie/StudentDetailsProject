package com.example.studentDetailsBackEnd.DTO;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class SportDetailRequest {
    private Integer studentID;
    private Integer eventID;
    private Integer eventCategoryID;
    private LocalDate eventDate;
    private String role;
    private String achievement;
    private String achievementDetails;
    private String otherDetails;
    private MultipartFile file;


    public SportDetailRequest() {}

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public Integer getEventCategoryID() {
        return eventCategoryID;
    }

    public void setEventCategoryID(Integer eventCategoryID) {
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
