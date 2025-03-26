package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.SportDetail;
import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.SportEvents;
import com.example.studentDetailsBackEnd.Model.SportEventCategory;
import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.repository.SportDetailRepository;
import com.example.studentDetailsBackEnd.repository.SportEventsRepository;
import com.example.studentDetailsBackEnd.repository.SportCategoryRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.Model.TableDetails;
import com.example.studentDetailsBackEnd.DTO.SportDetailRequest;
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

@RestController
@RequestMapping("/api/sport-details")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SportDetailController {

    @Autowired
    private SportDetailRepository sportDetailRepository;

    @Autowired
    private SportEventsRepository sportEventsRepository;

    @Autowired
    private SportCategoryRepository sportCategoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addSportDetail(@ModelAttribute SportDetailRequest request) {
    System.out.println("📥 Received Request: " + request);

    Optional<Student> studentOpt = studentRepository.findById(request.getStudentID());
    if (studentOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Student not found!");
    }
    Student student = studentOpt.get();

    Faculty faculty = student.getFaculty();
    if (faculty == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ No faculty assigned to student!");
    }

    Optional<SportEvents> eventOpt = sportEventsRepository.findById(request.getEventID());
    if (eventOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Sport Event not found!");
    }
    SportEvents event = eventOpt.get();

    Optional<SportEventCategory> categoryOpt = sportCategoryRepository.findById(request.getEventCategoryID());
    if (categoryOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Sport Event Category not found!");
    }
    SportEventCategory category = categoryOpt.get();

    SportDetail detail = new SportDetail(student, event, category, request.getEventDate(), 
                                         request.getRole(), request.getAchievement(), 
                                         request.getAchievementDetails(), request.getOtherDetails());
    detail.setStatus("PENDING");

    if (request.getFile() != null && !request.getFile().isEmpty()) {
        try {
            detail.setOfferLetter(request.getFile().getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error saving file");
        }
    }

    SportDetail savedDetail = sportDetailRepository.save(detail);
    int entryID = savedDetail.getSportDetailID();

    TableDetails tableDetails = tableDetailsRepository.findByTableName("sport_details");
    if (tableDetails == null) {
        return ResponseEntity.status(500).body("❌ Table entry for sport_details not found.");
    }
    int tableID = tableDetails.getTableID();

    Request newRequest = new Request();
    newRequest.setStudent(student);
    newRequest.setFaculty(faculty);
    newRequest.setTableDetails(tableDetails);
    newRequest.setEntryID(entryID);
    newRequest.setStatus("PENDING");
    requestRepository.save(newRequest);

    return ResponseEntity.ok("✅ Sport Detail added & Request sent for approval!");
}

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable int id) {
        Optional<SportDetail> detailOpt = sportDetailRepository.findById(id);  
        if (detailOpt.isEmpty() || detailOpt.get().getOfferLetter() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", "attachment; filename=\"offer_letter.pdf\"")
            .body(detailOpt.get().getOfferLetter());
}

    @GetMapping("/all")
    public ResponseEntity<List<SportDetail>> getAllSportDetails() {
        return ResponseEntity.ok(sportDetailRepository.findAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String status) {
        Optional<SportDetail> detailOpt = sportDetailRepository.findById(id);
        if (detailOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Sport Detail not found!");
        }

        SportDetail detail = detailOpt.get();
        detail.setStatus(status);
        sportDetailRepository.save(detail);

        return ResponseEntity.ok("✅ Status updated successfully!");
    }

    @GetMapping("/event-names")
    public ResponseEntity<List<Map<String, Object>>> getEventNames() {
        List<Map<String, Object>> events = sportEventsRepository.findAll()
                .stream()
                .map(event -> Map.of("eventID", event.getEventID(), "sportEventName", event.getSportEventName()))
                .toList();

        return ResponseEntity.ok(events);
    }

    @GetMapping("/event-categories")
    public ResponseEntity<List<Map<String, Object>>> getEventCategories() {
        List<Map<String, Object>> categories = sportCategoryRepository.findAll()
                .stream()
                .map(category -> Map.of("eventCategoryID", category.getSportEventCategoryID(), "sportCategoryName", category.getSportEventCategoryName()))
                .toList();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<SportEvents> getSportEventById(@PathVariable int id) {
        return sportEventsRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/event/add")
    public ResponseEntity<?> addSportEvent(@RequestBody SportEvents event) {
        if (event.getSportEventName() == null || event.getSportEventName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Event name cannot be empty.");
        }
        return ResponseEntity.ok(sportEventsRepository.save(event));
    }
}
