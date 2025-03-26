package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "placement_details")
public class PlacementDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer placementID;

    @ManyToOne
    @JoinColumn(name = "studentID", nullable = false)
    private Student student;

    private String placementType;  

    @ManyToOne
    @JoinColumn(name = "companyID", nullable = false)
    private PlacementCompanyDetail company;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "status", nullable = false)
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

    public PlacementDetail() {}

    public PlacementDetail(Integer placementID, Student student, String placementType, PlacementCompanyDetail company, LocalDate startDate, LocalDate endDate, String role) {
        this.placementID = placementID;
        this.student = student;
        this.placementType = placementType;
        this.company = company;
        this.startDate = startDate;
        this.endDate = endDate;
        this.role = role;
    }

    public Integer getPlacementID() {
        return placementID;
    }

    public void setPlacementID(Integer placementID) {
        this.placementID = placementID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {    
        this.student = student;
    }

    public String getPlacementType() {
        return placementType;
    }

    public void setPlacementType(String placementType) {    
        this.placementType = placementType;    
    }

    public PlacementCompanyDetail getCompany() {    
        return company;    
    }

    public void setCompany(PlacementCompanyDetail company) {        
        this.company = company;    
    }

    public LocalDate getStartDate() {       
        return startDate;    
    }

    public void setStartDate(LocalDate startDate) {        
        this.startDate = startDate;    
    }

    public LocalDate getEndDate() {        
        return endDate;    
    }

    public void setEndDate(LocalDate endDate) {        
        this.endDate = endDate;    
    }

    public String getRole() {        
        return role;    
    }

    public void setRole(String role) {        
        this.role = role;    
    }

    public String getStatus() {        
        return status;    
    }

    public void setStatus(String status) {        
        this.status = status;    
    }

    public String getRemark() {        
        return remark;    
    }    

    public void setRemark(String remark) {        
        this.remark = remark;    
    }
}
