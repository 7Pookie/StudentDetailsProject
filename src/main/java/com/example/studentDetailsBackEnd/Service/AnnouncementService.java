package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.Announcement;
import com.example.studentDetailsBackEnd.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service  // ✅ Added missing annotation
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Announcement addAnnouncement(String title, String content) {
        Announcement announcement = new Announcement(title, content);
        announcement.setDate(LocalDate.now());  // ✅ Fixed setter
        return announcementRepository.save(announcement);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByDateDesc();
    }
}
