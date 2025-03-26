package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "professional_society_details")
public class ProfessionalSocietyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="society_detailsid")
    private int societyDetailsID;

    @ManyToOne
    @JoinColumn(name = "studentID", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "societyID", nullable = false)
    private ProfessionalSociety society;

    @ManyToOne
    @JoinColumn(name = "fieldID", nullable = false)
    private ProfessionalSocietyField field;

    private LocalDate dateJoined;
    private String role;
    private String achievementDetails;
    private String status="PENDING";
    private String remark="";

    @Lob
    @Column(columnDefinition = "LONGBLOB", name="offer_letter")
    private byte[] offerLetter; 

    public byte[] getOfferLetter() {
        return offerLetter;
    }

    public void setOfferLetter(byte[] offerLetter) {
        this.offerLetter = offerLetter;
    }

    public int getSocietyDetailsID() { return societyDetailsID; }
    public void setSocietyDetailsID(int societyDetailsID) { this.societyDetailsID = societyDetailsID; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public ProfessionalSociety getSociety() { return society; }
    public void setSociety(ProfessionalSociety society) { this.society = society; }

    public ProfessionalSocietyField getField() { return field; }
    public void setField(ProfessionalSocietyField field) { this.field = field; }

    public LocalDate getDateJoined() { return dateJoined; }
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAchievementDetails() { return achievementDetails; }
    public void setAchievementDetails(String achievementDetails) { this.achievementDetails = achievementDetails; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemark() {return remark;}
    public void setRemark(String remark) {this.remark = remark;}
}
