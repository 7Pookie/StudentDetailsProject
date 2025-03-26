package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.CulturalDetailRequest;
import com.example.studentDetailsBackEnd.Model.CulturalDetail;
import com.example.studentDetailsBackEnd.Model.EventCategory;
import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.CulturalEvents;
import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.Service.CulturalDetailService;
import com.example.studentDetailsBackEnd.repository.EventCategoryRepository;
import com.example.studentDetailsBackEnd.repository.CulturalEventsRepository;
import com.example.studentDetailsBackEnd.repository.CulturalDetailRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/cultural-details")
public class CulturalDetailController {

    private final CulturalDetailService culturalDetailService;
    private final CulturalDetailRepository culturalDetailRepository;
    
    @Autowired
    private CulturalEventsRepository culturalEventsRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private StudentRepository studentRepository; 

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    public CulturalDetailController(CulturalDetailService culturalDetailService, 
                                     CulturalDetailRepository culturalDetailRepository) {
        this.culturalDetailService = culturalDetailService;
        this.culturalDetailRepository = culturalDetailRepository; 
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addCulturalDetail(@ModelAttribute CulturalDetailRequest request) {
    System.out.println("Received student ID: " + request.getStudentID());

    if (request.getStudentID() == 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid student ID received!");
    }

    Optional<Student> studentOpt = studentRepository.findById(request.getStudentID());
    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found!");
    }

    Student student = studentOpt.get();
    Faculty faculty = student.getFaculty();
    if (faculty == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No faculty assigned to student!");
    }

    CulturalEvents event = null;
    if (request.getEventID() != 0) {
        event = culturalEventsRepository.findById(request.getEventID()).orElse(null);
    }

    EventCategory eventCategory = null;
    if (request.getEventCategoryID() != 0) {
        eventCategory = eventCategoryRepository.findById(request.getEventCategoryID()).orElse(null);
    }

    CulturalDetail detail = new CulturalDetail();
    detail.setStudent(studentOpt.get());
    detail.setEvent(event);
    detail.setEventCategory(eventCategory);
    detail.setEventDate(request.getEventDate());
    detail.setRole(request.getRole());
    detail.setAchievement(request.getAchievement());
    detail.setAchievementDetails(request.getAchievementDetails());
    detail.setOtherDetails(request.getOtherDetails());

    if (request.getFile() != null && !request.getFile().isEmpty()) {
        try {
            detail.setOfferLetter(request.getFile().getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error saving file");
        }
    }

    CulturalDetail savedDetail = culturalDetailRepository.save(detail);
    int entryID = savedDetail.getCulturalDetailID();

    Optional<Integer> tableIDOpt = Optional.ofNullable(tableDetailsRepository.findByTableName("cultural_event_details"))
    .map(table -> table.getTableID());

    if (tableIDOpt.isEmpty()) {
    return ResponseEntity.status(500).body("Table entry for cultural_details not found.");
    }

    int tableID = tableIDOpt.get();

    Request newRequest = new Request();
    newRequest.setStudent(student);
    newRequest.setFaculty(faculty);
    newRequest.setTableDetails(tableDetailsRepository.findById(tableID).get());
    newRequest.setEntryID(entryID);
    newRequest.setStatus("PENDING");

    requestRepository.save(newRequest);

    return ResponseEntity.ok("Cultural Detail added & Request sent for approval!");
}

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable int id) {
        Optional<CulturalDetail> detailOpt = culturalDetailRepository.findById(id);  // Fixed static reference
        if (detailOpt.isEmpty() || detailOpt.get().getOfferLetter() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", "attachment; filename=\"offer_letter.pdf\"")
            .body(detailOpt.get().getOfferLetter());
}


    @GetMapping("/all")
    public ResponseEntity<List<CulturalDetail>> getAllCulturalDetails() {
        return ResponseEntity.ok(culturalDetailService.getAllCulturalDetails());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String status) {
        culturalDetailService.updateStatus(id, status);
        return ResponseEntity.ok("Status updated successfully!");
    }

    @GetMapping("/event-names")
    public ResponseEntity<List<Map<String, Object>>> getEventNames() {
        List<Map<String, Object>> events = culturalEventsRepository.findAll()
            .stream()
            .map(event -> Map.of("eventID", event.getEventID(), "name", event.getName()))
            .toList();
        
        return ResponseEntity.ok(events);
    }

    @GetMapping("/event-categories")
    public ResponseEntity<List<Map<String, Object>>> getEventCategories() {
        List<Map<String, Object>> categories = eventCategoryRepository.findAll()
            .stream()
            .map(category -> Map.of("eventCategoryID", category.getEventCategoryID(), "eventCategoryName", category.getEventCategoryName()))
            .toList();
        
        return ResponseEntity.ok(categories);
    }
}
