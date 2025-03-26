package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TechnicalDetailService {

    @Autowired
    private TechnicalDetailRepository technicalDetailRepository;

    @Autowired
    private TechnicalEventsRepository technicalEventsRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    public TechnicalDetail addTechnicalDetail(int studentId, String eventName, String eventCategory, LocalDate eventDate, String role, String achievement, String achievementDetails, String otherDetails) {
        System.out.println("Received studentId: " + studentId);
        // Fetch Student (Foreign Key)
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
    
        // Fetch or Create Technical Event
        TechnicalEvents event = technicalEventsRepository.findByName(eventName);
        if (event == null) {
            event = technicalEventsRepository.save(new TechnicalEvents(eventName));
        }
    
        // Fetch or Create Event Category
        EventCategory category = eventCategoryRepository.findByCategory(eventCategory);
        if (category == null) {
            category = eventCategoryRepository.save(new EventCategory(eventCategory));
        }
    
        // Save only Foreign Keys in `technical_event_details` table
        TechnicalDetail detail = new TechnicalDetail(student, event, category, eventDate, role, achievement, achievementDetails, otherDetails);
        detail.setStatus("PENDING");  // ✅ Explicitly setting the status before saving
        return technicalDetailRepository.save(detail);
    }
    

    public List<TechnicalDetail> getAllTechnicalDetails() {
        return technicalDetailRepository.findAll();
    }

    // ✅ Method to update status (e.g., "APPROVED" / "REJECTED")
    public TechnicalDetail updateStatus(int id, String status) {
        TechnicalDetail detail = technicalDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technical Detail not found!"));
        detail.setStatus(status);
        return technicalDetailRepository.save(detail);
    }
}
