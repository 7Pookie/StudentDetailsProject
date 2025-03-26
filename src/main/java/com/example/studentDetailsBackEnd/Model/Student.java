package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String dob;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String rollNo;

    @Column(nullable = false)
    private String program;

    @Column(unique = true)
    private String apaarid;

    @ManyToOne
    @JoinColumn(name = "facultyID", referencedColumnName = "facultyID", foreignKey = @jakarta.persistence.ForeignKey(name = "student_facultyID"))
    private Faculty facultyID;

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getEmail() {
        return email;
    }

    public int getStudentID() {
        return studentID;
    }
}
