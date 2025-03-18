package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.TableDetails;
import com.example.studentDetailsBackEnd.Service.RequestService;
import com.example.studentDetailsBackEnd.repository.FacultyRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    // ✅ Submit Request (Student Submits an Entry for Faculty Approval)
    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestBody Request requestData) {
        Optional<Student> studentOpt = studentRepository.findById(requestData.getStudent().getStudentID());
        Optional<Faculty> facultyOpt = facultyRepository.findById(requestData.getFaculty().getFacultyID());
        Optional<TableDetails> tableDetailsOpt = tableDetailsRepository.findById(requestData.getTableDetails().getTableID());

        if (studentOpt.isEmpty() || facultyOpt.isEmpty() || tableDetailsOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid student, faculty, or table ID.");
        }

        Request request = new Request();
        request.setStudent(studentOpt.get());
        request.setFaculty(facultyOpt.get());
        request.setTableDetails(tableDetailsOpt.get());
        request.setEntryID(requestData.getEntryID());

        requestService.saveRequest(request);
        return ResponseEntity.ok("✅ Request submitted successfully!");
    }

    // ✅ Fetch Requests for a Faculty
    @GetMapping("/faculty/{facultyID}")
    public ResponseEntity<List<Request>> getRequestsForFaculty(@PathVariable int facultyID) {
        List<Request> requests = requestService.getRequestsForFaculty(facultyID);
        return ResponseEntity.ok(requests);
    }
}
