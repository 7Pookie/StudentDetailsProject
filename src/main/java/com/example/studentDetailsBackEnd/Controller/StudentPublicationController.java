package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.StudentPublication;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.TableDetails;
import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.StudentPublicationRepository;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/student-publications")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StudentPublicationController {

    @Autowired private StudentPublicationRepository publicationRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TableDetailsRepository tableDetailsRepository;
    @Autowired private RequestRepository requestRepository;

    /**
     * ✅ Add a new student publication
     */
    @PostMapping("/add")
    @Transactional
    public ResponseEntity<?> addPublication(@RequestBody Map<String, Object> requestBody) {
        System.out.println("📥 Received Publication Request: " + requestBody);

    // ✅ Extract Student ID correctly
        Map<String, Object> studentMap = (Map<String, Object>) requestBody.get("student");
        if (studentMap == null || !studentMap.containsKey("studentID")) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ Student ID is missing."));
        }

        int studentID = (int) studentMap.get("studentID");

    // ✅ Extract publication fields
        String title = (String) requestBody.get("title");
        String authors = (String) requestBody.get("authors");
        String type = (String) requestBody.get("type");
        String publicationStatus = (String) requestBody.get("publicationStatus");
        String number = (String) requestBody.get("number");

        LocalDate publicationDate;
        try {
            publicationDate = LocalDate.parse((String) requestBody.get("publicationDate"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ Invalid publication date format."));
        }

    // ✅ Validate required fields
        if (title == null || title.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ Publication title is required."));
        }

        if (publicationDate == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ Publication date is required."));
        }

    // ✅ Fetch Student
        Optional<Student> studentOpt = studentRepository.findById(studentID);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "❌ Student not found."));
        }
        Student student = studentOpt.get();

    // ✅ Validate Faculty
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ No faculty assigned to student!"));
        }

    // ✅ Save StudentPublication Object
        StudentPublication newPublication = new StudentPublication();
        newPublication.setStudent(student);
        newPublication.setTitle(title);
        newPublication.setAuthors(authors);
        newPublication.setType(type);
        newPublication.setPublicationDate(publicationDate);
        newPublication.setPublicationStatus(publicationStatus);
        newPublication.setNumber(number);
        newPublication.setStatus("PENDING");

        StudentPublication savedPublication = publicationRepository.save(newPublication);
        int entryID = savedPublication.getStudentPubID();
        System.out.println("✅ Publication saved: " + savedPublication);

    // ✅ Fetch Table ID
        TableDetails tableDetails = tableDetailsRepository.findByTableName("student_publications");
        if (tableDetails == null) {
            return ResponseEntity.status(500).body("❌ Table entry for student_publications not found.");
        }
        int tableID = tableDetails.getTableID();

    // ✅ Create Request Entry
        Request newRequest = new Request();
        newRequest.setStudent(student);
        newRequest.setFaculty(faculty);
        newRequest.setTableDetails(tableDetails);
        newRequest.setEntryID(entryID);
        newRequest.setStatus("PENDING");
        requestRepository.save(newRequest);

        return ResponseEntity.ok(Map.of("message", "✅ Publication added & Request sent for approval!", "publication", savedPublication));
    }

    /**
     * ✅ Get all publications for a specific student
     */
    @GetMapping("/{studentID}")
    public ResponseEntity<List<StudentPublication>> getPublications(@PathVariable int studentID) {
        return ResponseEntity.ok(publicationRepository.findByStudent_StudentID(studentID));
    }

    /**
     * ✅ Update publication status (e.g., Approved/Rejected)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String status) {
        Optional<StudentPublication> pubOpt = publicationRepository.findById(id);
        if (pubOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Publication not found!");
        }

        StudentPublication publication = pubOpt.get();
        publication.setStatus(status);
        publicationRepository.save(publication);

        return ResponseEntity.ok("✅ Publication status updated successfully!");
    }
}
