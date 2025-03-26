package com.example.studentDetailsBackEnd.DTO;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class ProfessionalSocietyDetailRequest {
    private Integer studentID;
    private Integer societyID;
    private String customSocietyName;
    private Integer fieldID;
    private String customFieldName;
    private LocalDate dateJoined;
    private String role;
    private String achievementDetails;
    private MultipartFile file;


    public Integer getStudentID() { return studentID; }
    public void setStudentID(Integer studentID) { this.studentID = studentID; }

    public Integer getSocietyID() { return societyID; }
    public void setSocietyID(Integer societyID) { this.societyID = societyID; }

    public String getCustomSocietyName() { return customSocietyName; }
    public void setCustomSocietyName(String customSocietyName) { this.customSocietyName = customSocietyName; }

    public Integer getFieldID() { return fieldID; }
    public void setFieldID(Integer fieldID) { this.fieldID = fieldID; }

    public String getCustomFieldName() { return customFieldName; }
    public void setCustomFieldName(String customFieldName) { this.customFieldName = customFieldName; }

    public LocalDate getDateJoined() { return dateJoined; }
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAchievementDetails() { return achievementDetails; }
    public void setAchievementDetails(String achievementDetails) { this.achievementDetails = achievementDetails; }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
