package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.StudentPublicationDTO;
import com.example.studentDetailsBackEnd.Model.StudentPublication;
import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.repository.StudentPublicationRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/student-publications")
public class StudentPublicationController {

    private final StudentPublicationRepository studentPublicationRepository;
    private final StudentRepository studentRepository;
    private final TableDetailsRepository tableDetailsRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public StudentPublicationController(StudentPublicationRepository studentPublicationRepository,
                                     StudentRepository studentRepository,
                                     TableDetailsRepository tableDetailsRepository,
                                     RequestRepository requestRepository) {
        this.studentPublicationRepository = studentPublicationRepository;
        this.studentRepository = studentRepository;
        this.tableDetailsRepository = tableDetailsRepository;
        this.requestRepository = requestRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPublication(@ModelAttribute StudentPublicationDTO request) {
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

        StudentPublication publication = new StudentPublication();
        publication.setStudent(student);
        publication.setTitle(request.getTitle());
        publication.setAuthors(request.getAuthors());
        publication.setType(request.getType());
        publication.setPublicationDate(request.getPublicationDate());
        publication.setPublicationStatus(request.getPublicationStatus());
        publication.setNumber(request.getNumber());
        publication.setStatus("PENDING");

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            try {
                publication.setOfferLetter(request.getFile().getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error saving file");
            }
        }

        StudentPublication savedPublication = studentPublicationRepository.save(publication);
        int entryID = savedPublication.getStudentPubID();

        Optional<Integer> tableIDOpt = Optional.ofNullable(tableDetailsRepository.findByTableName("student_publications"))
                .map(table -> table.getTableID());

        if (tableIDOpt.isEmpty()) {
            return ResponseEntity.status(500).body("Table entry for student_publications not found.");
        }

        int tableID = tableIDOpt.get();

        Request newRequest = new Request();
        newRequest.setStudent(student);
        newRequest.setFaculty(faculty);
        newRequest.setTableDetails(tableDetailsRepository.findById(tableID).get());
        newRequest.setEntryID(entryID);
        newRequest.setStatus("PENDING");

        requestRepository.save(newRequest);

        return ResponseEntity.ok("Publication added & Request sent for approval!");
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable int id) {
        Optional<StudentPublication> publicationOpt = studentPublicationRepository.findById(id);
        if (publicationOpt.isEmpty() || publicationOpt.get().getOfferLetter() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=\"offer_letter.pdf\"")
                .body(publicationOpt.get().getOfferLetter());
    }

    @GetMapping("/{studentID}")
    public ResponseEntity<List<StudentPublication>> getPublicationsByStudent(@PathVariable int studentID) {
        return ResponseEntity.ok(studentPublicationRepository.findByStudent_StudentID(studentID));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String status) {
        Optional<StudentPublication> publicationOpt = studentPublicationRepository.findById(id);
        if (publicationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publication not found!");
        }

        StudentPublication publication = publicationOpt.get();
        publication.setStatus(status);
        studentPublicationRepository.save(publication);

        return ResponseEntity.ok("Publication status updated successfully!");
    }
}