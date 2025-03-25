package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CulturalDetailService {

    @Autowired
    private CulturalDetailRepository culturalDetailRepository;

    @Autowired
    private CulturalEventsRepository culturalEventsRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    public CulturalDetail addCulturalDetail(int studentId, String eventName, String eventCategory, LocalDate eventDate, String role, String achievement, String achievementDetails, String otherDetails) {
        System.out.println("Received studentId: " + studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
    
        CulturalEvents event = culturalEventsRepository.findByName(eventName);
        if (event == null) {
            event = culturalEventsRepository.save(new CulturalEvents(eventName));
        }
    
        EventCategory category = eventCategoryRepository.findByCategory(eventCategory);
        if (category == null) {
            category = eventCategoryRepository.save(new EventCategory(eventCategory));
        }
    
        CulturalDetail detail = new CulturalDetail(student, event, category, eventDate, role, achievement, achievementDetails, otherDetails);
        detail.setStatus("PENDING");
        return culturalDetailRepository.save(detail);
    }
    

    public List<CulturalDetail> getAllCulturalDetails() {
        return culturalDetailRepository.findAll();
    }

    public CulturalDetail updateStatus(int id, String status) {
        CulturalDetail detail = culturalDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cultural Detail not found!"));
        detail.setStatus(status);
        return culturalDetailRepository.save(detail);
    }
}
