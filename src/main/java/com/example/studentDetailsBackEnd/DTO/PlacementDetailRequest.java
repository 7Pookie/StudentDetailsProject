package com.example.studentDetailsBackEnd.DTO;

import java.time.LocalDate;

public class PlacementDetailRequest {
    private Integer studentID;
    private String placementType;
    private Integer companyID;
    private LocalDate startDate;
    private LocalDate endDate;
    private String role;
    private String customCompanyName; 
    private String status;  
    private String remark;  

    public PlacementDetailRequest() {}

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {        
        this.studentID = studentID;    
    }

    public String getPlacementType() {
        return placementType;
    }

    public void setPlacementType(String placementType) {        
        this.placementType = placementType;    
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {        
        this.companyID = companyID;    
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

    public String getCustomCompanyName() {
        return customCompanyName;
    }

    public void setCustomCompanyName(String customCompanyName) {
        this.customCompanyName = customCompanyName;    
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
