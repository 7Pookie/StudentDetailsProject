package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SportDetailService {

    @Autowired
    private SportDetailRepository sportDetailRepository;

    @Autowired
    private SportEventsRepository sportEventsRepository;

    @Autowired
    private SportCategoryRepository sportCategoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    public SportDetail addSportDetail(int studentId, int eventID, int eventCategoryID, LocalDate eventDate, 
                                      String role, String achievement, String achievementDetails, String otherDetails) {
        System.out.println("Received studentId: " + studentId);
    
        // 🔹 Fetch Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("❌ Student not found in the database!"));

        // 🔹 Fetch Event using eventID (instead of eventName)
        SportEvents event = sportEventsRepository.findById(eventID)
                .orElseThrow(() -> new RuntimeException("❌ Event ID not found in the database!"));

        // 🔹 Fetch Category using eventCategoryID (instead of eventCategory)
        SportEventCategory category = sportCategoryRepository.findById(eventCategoryID)
                .orElseThrow(() -> new RuntimeException("❌ Event Category ID not found in the database!"));
    
        // 🔹 Create SportDetail
        SportDetail detail = new SportDetail(student, event, category, eventDate, role, achievement, achievementDetails, otherDetails);
        detail.setStatus("PENDING");  // Default status

        return sportDetailRepository.save(detail);
    }
    
    public List<SportDetail> getAllSportDetails() {
        return sportDetailRepository.findAll();
    }

    public SportDetail updateStatus(int id, String status) {
        SportDetail detail = sportDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Sport Detail not found!"));
        detail.setStatus(status);
        return sportDetailRepository.save(detail);
    }
}
