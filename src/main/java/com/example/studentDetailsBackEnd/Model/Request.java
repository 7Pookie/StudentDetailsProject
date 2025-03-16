package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestID;

    @ManyToOne
    @JoinColumn(name = "studentID", referencedColumnName = "studentID", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "facultyID", referencedColumnName = "facultyID", nullable = false)
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "tableID", referencedColumnName = "tableID", nullable = false)
    private TableDetails tableDetails;

    @Column(nullable = false)
    private int entryID;
}
