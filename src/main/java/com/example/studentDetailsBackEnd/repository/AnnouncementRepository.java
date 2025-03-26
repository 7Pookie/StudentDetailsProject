package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    List<Announcement> findAllByOrderByDateDesc(); // Get latest announcements first
}
