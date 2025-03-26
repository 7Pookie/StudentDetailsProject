package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "faculty_publications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacultyPublication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facultyPubID")
    private int facultyPubID;

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

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getNumber() {
        return number;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublicationStatus() {
        return publicationStatus;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPublicationStatus(String publicationStatus) {
        this.publicationStatus = publicationStatus;
    }
}
