package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "club_detail")
public class ClubDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clubDetailID;

    @ManyToOne
    @JoinColumn(name = "studentID", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "clubID", nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "clubCategoryID", nullable = false)
    private ClubCategory clubCategory;

    @Column(nullable = false, length = 45)
    private String position;

    @Column(nullable = false, length = 45)
    private String dateJoined;

    @Column(length = 45)
    private String otherDetails;

    @Column(length = 45)
    private String link;

    @Column(length = 45)
    private String status;
}