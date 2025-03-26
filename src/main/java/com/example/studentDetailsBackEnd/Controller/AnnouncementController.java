package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.Announcement;
import com.example.studentDetailsBackEnd.Service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // ✅ Only Admins can add an announcement
    @PostMapping("/add")
    public ResponseEntity<String> addAnnouncement(@RequestBody Announcement announcement) {
        announcementService.addAnnouncement(announcement.getTitle(), announcement.getContent());
        return ResponseEntity.ok("Announcement added successfully!");
    }

    // ✅ All users can see announcements
    @GetMapping("/all")
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }
}
