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

    @Lob
    @Column(columnDefinition = "LONGBLOB", name="offer_letter")
    private byte[] offerLetter;

    public byte[] getOfferLetter() {
        return offerLetter;
    }

    public void setOfferLetter(byte[] offerLetter) {
        this.offerLetter = offerLetter;
    }
    
    public void setStudentById(int studentID) {
        this.student = new Student();
        this.student.setStudentID(studentID);
    }

    public String getRemark()
    {
        return remark;
    }

    public String getTitle()
    {
        return title;
    }

    public String getType()
    {
        return type;
    }

    public String getAuthors()
    {
        return authors;
    }

    public LocalDate getPublicationDate(){
        return publicationDate;
    }

    public String getNumber(){
        return number;
    }

    public String getPublicationStatus(){
        return publicationStatus;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setStudent(Student student) {    
        this.student = student;
    }

    public void setAuthors(String authors){
        this.authors = authors;
    }

    public void setPublicationDate(LocalDate publicationDate){
        this.publicationDate= publicationDate;
    }

    public void setPublicationStatus(String publicationStatus){
        this.publicationStatus= publicationStatus;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setStudentPubID(int studentPubID ){
        this.studentPubID = studentPubID;
    }

    public int getStudentPubID(){
        return studentPubID;
    }
}
