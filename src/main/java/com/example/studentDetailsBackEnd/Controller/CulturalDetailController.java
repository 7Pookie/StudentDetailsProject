package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.CulturalDetailRequest;
import com.example.studentDetailsBackEnd.Model.CulturalDetail;
import com.example.studentDetailsBackEnd.Model.EventCategory;
import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.CulturalEvents;
import com.example.studentDetailsBackEnd.Service.CulturalDetailService;
import com.example.studentDetailsBackEnd.repository.EventCategoryRepository;
import com.example.studentDetailsBackEnd.repository.CulturalEventsRepository;
import com.example.studentDetailsBackEnd.repository.CulturalDetailRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/cultural-details")
public class CulturalDetailController{

    private final CulturalDetailService culturalDetailService;
    private final CulturalDetailRepository culturalDetailRepository;
    
    @Autowired
    private CulturalEventsRepository culturalEventsRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private StudentRepository studentRepository; 

    @Autowired
    public CulturalDetailController(CulturalDetailService culturalDetailService, 
    CulturalDetailRepository culturalDetailRepository) {
        this.culturalDetailService = culturalDetailService;
        this.culturalDetailRepository = culturalDetailRepository; 
    }

    // ✅ Add a new cultural detail
    
    @PostMapping("/add")
    public ResponseEntity<?> addTechnicalDetail(@RequestBody CulturalDetailRequest request) {
    System.out.println("Received student ID: " + request.getStudentID());

    if (request.getStudentID() == 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid student ID received!");
    }

    // Retrieve student
    Optional<Student> studentOpt = studentRepository.findById(request.getStudentID());
    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found!");
    }

    // Retrieve event (if exists)
    CulturalEvents event = null;
    if (request.getEventID() != 0) {
        event = culturalEventsRepository.findById(request.getEventID()).orElse(null);
    }

    // Retrieve event category (if exists)
    EventCategory eventCategory = null;
    if (request.getEventCategoryID() != 0) {
        eventCategory = eventCategoryRepository.findById(request.getEventCategoryID()).orElse(null);
    }

    // Create new CulturalDetail
    CulturalDetail detail = new CulturalDetail();
    detail.setStudent(studentOpt.get());
    detail.setEvent(event);
    detail.setEventCategory(eventCategory);
    detail.setEventDate(request.getEventDate());
    detail.setRole(request.getRole());
    detail.setAchievement(request.getAchievement());
    detail.setAchievementDetails(request.getAchievementDetails());
    detail.setOtherDetails(request.getOtherDetails());

    // Save to database
    culturalDetailRepository.save(detail);
    return ResponseEntity.ok("Cultural Detail added successfully!");
}


    // ✅ Get all cultural details
    @GetMapping("/all")
    public ResponseEntity<List<CulturalDetail>> getAllCulturalDetails() {
        return ResponseEntity.ok(culturalDetailService.getAllCulturalDetails());
    }

    // ✅ Update status of cultural detail
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String status) {
        culturalDetailService.updateStatus(id, status);
        return ResponseEntity.ok("Status updated successfully!");
    }

    // ✅ Fetch event names
    @GetMapping("/event-names")
    public ResponseEntity<List<Map<String, Object>>> getEventNames() {
        List<Map<String, Object>> events = culturalEventsRepository.findAll()
            .stream()
            .map(event -> Map.of("eventID", event.getEventID(), "name", event.getName()))
            .toList();
        
        return ResponseEntity.ok(events);
    }

    // ✅ Fetch event categories
    @GetMapping("/event-categories")
    public ResponseEntity<List<Map<String, Object>>> getEventCategories() {
        List<Map<String, Object>> categories = eventCategoryRepository.findAll()
            .stream()
            .map(category -> Map.of("eventCategoryID", category.getEventCategoryID(), "eventCategoryName", category.getEventCategoryName()))
            .toList();
        
        return ResponseEntity.ok(categories);
    }
}
