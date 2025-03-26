package com.example.studentDetailsBackEnd.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int announcementID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    public Announcement() {
        this.date = LocalDate.now(); // Automatically set date
    }

    public Announcement(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = LocalDate.now();
    }

    public int getAnnouncementID() {  // ✅ Corrected method name
        return announcementID;
    }

    public void setAnnouncementID(int id) {  // ✅ Corrected setter method
        this.announcementID = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {  // ✅ Accepts `LocalDate`, not `Date`
        this.date = date;
    }
}
