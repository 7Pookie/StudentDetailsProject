package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Service.StudentService;
import com.example.studentDetailsBackEnd.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")  // ✅ Change to match frontend request
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StudentController {

    private final StudentService studentService;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ✅ API to get Student Details (Using Session)
    @GetMapping("/details")
    public ResponseEntity<?> getStudentDetails(HttpSession session) {
        String email = (String) session.getAttribute("EMAIL");
        
        System.out.println("🔹 Session EMAIL: " + email); // ✅ Debugging
    
        if (email == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Session expired. Please log in again."));
        }
    
        Optional<Student> student = studentService.getStudentByEmail(email);
    
        if (student.isPresent()) {
            return ResponseEntity.ok(Map.of(
                "name", student.get().getName(),
                "rollNo", student.get().getRollNo(),
                "email", student.get().getEmail()
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Student not found."));
        }
    }
    

    // ✅ API to Get Current Student after Google OAuth
    @GetMapping("/me")
public ResponseEntity<?> getCurrentStudent(@AuthenticationPrincipal OAuth2User principal, HttpSession session) {
    if (principal == null) {
        System.out.println("❌ User not authenticated");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    System.out.println("🔹 OAuth2 User Details: " + principal.getAttributes());

    String email = principal.getAttribute("email");

    if (email == null) {
        System.out.println("❌ Email not found in OAuth response");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found in OAuth response");
    }

    Optional<Student> studentOpt = studentRepository.findByEmail(email);

    if (studentOpt.isEmpty()) {
        System.out.println("❌ Student not found in database for email: " + email);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found in database!");
    }

    // ✅ Store the email in session
    session.setAttribute("EMAIL", email);
    
    // ✅ Return Student ID
    Student student = studentOpt.get();
    System.out.println("✅ Found student: " + student.getStudentID());
    return ResponseEntity.ok(Map.of("studentID", student.getStudentID()));
}

}
