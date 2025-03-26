package com.example.studentDetailsBackEnd.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.studentDetailsBackEnd.repository.AdminRepository;
import com.example.studentDetailsBackEnd.repository.FacultyRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;

    public AuthController(StudentRepository studentRepository, FacultyRepository facultyRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.adminRepository = adminRepository;
    }

    // ✅ Step 1: Store selected role and redirect to login page
    @PostMapping("/select-role")
    public ResponseEntity<Map<String, String>> selectRole(@RequestBody RoleRequest request, HttpSession session) {
        if (request.getRole() == null || request.getRole().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Role is required."));
        }

        String role = request.getRole();
        session.setAttribute("USER_ROLE", role);
        System.out.println("✅ Stored Role in session: " + role);

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "http://localhost:5173/" + role + "/login");

        return ResponseEntity.ok(response);
    }

    // ✅ Step 2: Handle login and verify email
    @PostMapping("/validate-role")
    public ResponseEntity<Map<String, String>> validateRole(@RequestBody RoleRequest request, HttpSession session) {
        String email = (String) session.getAttribute("EMAIL");

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Session expired. Please log in again."));
        }

        boolean exists = false;
        switch (request.getRole()) {
            case "student":
                exists = studentRepository.existsByEmail(email);
                break;
            case "faculty":
                exists = facultyRepository.existsByEmail(email);
                break;
            case "admin":
                exists = adminRepository.existsByEmail(email);
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid role selected."));
        }

        if (!exists) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You are not registered in this role.", "redirectUrl", "/select-role"));
        }

        String dashboardUrl = "http://localhost:5173/" + request.getRole() + "/dashboard";
        return ResponseEntity.ok(Map.of("redirectUrl", dashboardUrl));
    }

    // ✅ Utility: Get logged-in user details
    @GetMapping("/get-user")
    public ResponseEntity<Map<String, String>> getUser(HttpSession session) {
        String email = (String) session.getAttribute("EMAIL");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in."));
        }
        return ResponseEntity.ok(Map.of("email", email));
    }

    // ✅ Class to handle role request JSON
    static class RoleRequest {
        private String role;
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
