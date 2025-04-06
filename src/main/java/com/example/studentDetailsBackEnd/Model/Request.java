package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestID;

    @ManyToOne
    @JoinColumn(name = "studentID", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "facultyID", nullable = false)
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "tableID", nullable = false)
    private TableDetails tableDetails;

    @Column(nullable = false)
    private int entryID;

    @Column(nullable = false)
    private String status = "PENDING";

    // Getters and Setters
    public int getRequestID() { return requestID; }
    public void setRequestID(int requestID) { this.requestID = requestID; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public TableDetails getTableDetails() { return tableDetails; }
    public void setTableDetails(TableDetails tableDetails) { this.tableDetails = tableDetails; }
    public int getEntryID() { return entryID; }
    public void setEntryID(int entryID) { this.entryID = entryID; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}