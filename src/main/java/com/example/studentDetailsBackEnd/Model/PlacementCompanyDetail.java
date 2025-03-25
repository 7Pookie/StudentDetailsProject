package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "placement_company_details")
public class PlacementCompanyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "companyID")
    private Integer companyID;

    @Column(name= "company_name", nullable = false, unique = true)
    private String companyName;

    public PlacementCompanyDetail() {}

    public PlacementCompanyDetail(String companyName) {
        this.companyName = companyName;
    }
    public Integer getCompanyID() {
        return companyID;
    }    

    public void setCompanyID(Integer companyID) {        
        this.companyID = companyID;    
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {        
        this.companyName = companyName;    
    }    
}