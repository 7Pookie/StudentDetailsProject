package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_publications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPublication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_pubid", nullable = false)
    private int studentPubID;

    @ManyToOne
    @JoinColumn(name = "studentid", referencedColumnName = "studentID", foreignKey = @ForeignKey(name = "publication_studentID"), nullable = false)
    private Student student;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "publicationDate", nullable = false)
    private LocalDate publicationDate;

    @Column(name = "isbn_issn", nullable = false)
    private String number;

    @Column(name = "authors", nullable = false)
    private String authors;

    @Column(name = "publicationStatus", nullable = false)
    private String publicationStatus;

    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @Column(name = "remark")
    private String remark = "";

    // ✅ Helper method to set student by ID
    public void setStudentById(int studentID) {
        this.student = new Student();
        this.student.setStudentID(studentID);
    }
}
