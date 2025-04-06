package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.CulturalDetailRequest;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import com.example.studentDetailsBackEnd.Service.CulturalDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/cultural-details")
public class CulturalDetailController {

    @Autowired
    private CulturalDetailService culturalDetailService;

    @Autowired
    private CulturalDetailRepository culturalDetailRepository;

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

        CulturalEvents event = request.getEventID() != 0 ? culturalEventsRepository.findById(request.getEventID()).orElse(null) : null;
        EventCategory eventCategory = request.getEventCategoryID() != 0 ? eventCategoryRepository.findById(request.getEventCategoryID()).orElse(null) : null;

        CulturalDetail detail = new CulturalDetail();
        detail.setStudent(student);
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

        Optional<TableDetails> tableOpt = Optional.ofNullable(tableDetailsRepository.findByTableName("cultural_event_details"));
        if (tableOpt.isEmpty()) {
            return ResponseEntity.status(500).body("Table entry for cultural_details not found.");
        }

        Request newRequest = new Request();
        newRequest.setStudent(student);
        newRequest.setFaculty(faculty);
        newRequest.setTableDetails(tableOpt.get());
        newRequest.setEntryID(entryID);
        newRequest.setStatus("PENDING");

        requestRepository.save(newRequest);

        return ResponseEntity.ok("Cultural Detail added & Request sent for approval!");
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable int id) {
        Optional<CulturalDetail> detailOpt = culturalDetailRepository.findById(id);
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
