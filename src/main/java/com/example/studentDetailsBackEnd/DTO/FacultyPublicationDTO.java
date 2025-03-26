package com.example.studentDetailsBackEnd.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class FacultyPublicationDTO {
    private String title;
    private String type;
    private LocalDate publicationDate;
    private String number;  // ISBN/ISSN
    private String authors;
    private String publicationStatus;
    public FacultyPublicationDTO() {}

    public FacultyPublicationDTO(String title, String type, LocalDate publicationDate, String number, String authors, String publicationStatus) {
        this.title = title;
        this.type = type;
        this.publicationDate = publicationDate;
        this.number = number;
        this.authors = authors;
        this.publicationStatus = publicationStatus;
    }

    // Getters
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
