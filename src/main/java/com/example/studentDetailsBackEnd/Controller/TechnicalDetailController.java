package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.TechnicalDetailRequest;
import com.example.studentDetailsBackEnd.Model.TechnicalDetail;
import com.example.studentDetailsBackEnd.Model.EventCategory;
import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.TechnicalEvents;
import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.Service.TechnicalDetailService;
import com.example.studentDetailsBackEnd.repository.EventCategoryRepository;
import com.example.studentDetailsBackEnd.repository.TechnicalEventsRepository;
import com.example.studentDetailsBackEnd.repository.TechnicalDetailRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/technical-details")
public class TechnicalDetailController {

    private final TechnicalDetailService technicalDetailService;
    private final TechnicalDetailRepository technicalDetailRepository;
    
    @Autowired
    private TechnicalEventsRepository technicalEventsRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private StudentRepository studentRepository; 

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    public TechnicalDetailController(TechnicalDetailService technicalDetailService, 
                                     TechnicalDetailRepository technicalDetailRepository) {
        this.technicalDetailService = technicalDetailService;
        this.technicalDetailRepository = technicalDetailRepository; 
    }

    // ✅ Add a new technical detail
    
    @PostMapping("/add")
    public ResponseEntity<?> addTechnicalDetail(@RequestBody TechnicalDetailRequest request) {
    System.out.println("Received student ID: " + request.getStudentID());

    if (request.getStudentID() == 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid student ID received!");
    }

    // Retrieve student
    Optional<Student> studentOpt = studentRepository.findById(request.getStudentID());
    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found!");
    }

    Student student = studentOpt.get();
    Faculty faculty = student.getFaculty();
    if (faculty == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No faculty assigned to student!");
    }

    // Retrieve event (if exists)
    TechnicalEvents event = null;
    if (request.getEventID() != 0) {
        event = technicalEventsRepository.findById(request.getEventID()).orElse(null);
    }

    // Retrieve event category (if exists)
    EventCategory eventCategory = null;
    if (request.getEventCategoryID() != 0) {
        eventCategory = eventCategoryRepository.findById(request.getEventCategoryID()).orElse(null);
    }

    // Create new TechnicalDetail
    TechnicalDetail detail = new TechnicalDetail();
    detail.setStudent(studentOpt.get());
    detail.setEvent(event);
    detail.setEventCategory(eventCategory);
    detail.setEventDate(request.getEventDate());
    detail.setRole(request.getRole());
    detail.setAchievement(request.getAchievement());
    detail.setAchievementDetails(request.getAchievementDetails());
    detail.setOtherDetails(request.getOtherDetails());

    TechnicalDetail savedDetail = technicalDetailRepository.save(detail);
    int entryID = savedDetail.getTechnicalDetailID();

    Optional<Integer> tableIDOpt = Optional.ofNullable(tableDetailsRepository.findByTableName("technical_event_details"))
    .map(table -> table.getTableID());

    if (tableIDOpt.isEmpty()) {
    return ResponseEntity.status(500).body("Table entry for technical_details not found.");
    }

    int tableID = tableIDOpt.get();

    // ✅ Insert request for faculty approval
    Request newRequest = new Request();
    newRequest.setStudent(student);
    newRequest.setFaculty(faculty);
    newRequest.setTableDetails(tableDetailsRepository.findById(tableID).get());
    newRequest.setEntryID(entryID);
    newRequest.setStatus("PENDING");

    requestRepository.save(newRequest);

    return ResponseEntity.ok("Technical Detail added & Request sent for approval!");
}


    // ✅ Get all technical details
    @GetMapping("/all")
    public ResponseEntity<List<TechnicalDetail>> getAllTechnicalDetails() {
        return ResponseEntity.ok(technicalDetailService.getAllTechnicalDetails());
    }

    // ✅ Update status of technical detail
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String status) {
        technicalDetailService.updateStatus(id, status);
        return ResponseEntity.ok("Status updated successfully!");
    }

    // ✅ Fetch event names
    @GetMapping("/event-names")
    public ResponseEntity<List<Map<String, Object>>> getEventNames() {
        List<Map<String, Object>> events = technicalEventsRepository.findAll()
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
