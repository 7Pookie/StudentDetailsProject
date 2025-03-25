package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professional_society_field")
public class ProfessionalSocietyField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fieldID")
    private int fieldID;
    
    @Column(name = "field_name")
    private String fieldName;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<ProfessionalSocietyDetail> details;

    // Getters and Setters
    public int getFieldID() { return fieldID; }
    public void setFieldID(int fieldID) { this.fieldID = fieldID; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
}
