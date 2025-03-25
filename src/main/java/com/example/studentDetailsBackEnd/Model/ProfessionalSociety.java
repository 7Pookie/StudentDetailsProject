package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professional_societies")
public class ProfessionalSociety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="societyID")
    private int societyID;
    
    @Column(name="society_name")
    private String societyName;

    @OneToMany(mappedBy = "society", cascade = CascadeType.ALL)
    private List<ProfessionalSocietyDetail> details;

    // Getters and Setters
    public int getSocietyID() { return societyID; }
    public void setSocietyID(int societyID) { this.societyID = societyID; }

    public String getSocietyName() { return societyName; }
    public void setSocietyName(String societyName) { this.societyName = societyName; }
}
