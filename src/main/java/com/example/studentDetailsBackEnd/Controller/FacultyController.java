package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.Model.TechnicalDetail;
import com.example.studentDetailsBackEnd.repository.TechnicalDetailRepository;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.repository.TechnicalDetailRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;
import com.example.studentDetailsBackEnd.repository.FacultyRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.repository.FacultyRepository;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FacultyController {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Autowired
    private TechnicalDetailRepository technicalDetailRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentFaculty(@AuthenticationPrincipal OAuth2User principal, HttpSession session) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ User not authenticated");
        }
    
        String email = principal.getAttribute("email");
        Optional<Faculty> facultyOpt = facultyRepository.findByEmail(email);
    
        if (facultyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Faculty not found in database!");
        }
    
        Faculty faculty = facultyOpt.get();
        session.setAttribute("Email", email);
    
        System.out.println("✅ Faculty ID from session: " + faculty.getFacultyID());
    
        return ResponseEntity.ok(Map.of("facultyID", faculty.getFacultyID()));
    }
    

    @GetMapping("/requests/{facultyID}")
    public ResponseEntity<?> getPendingRequests(@PathVariable int facultyID) {
        List<Request> pendingRequests = requestRepository.findByFacultyFacultyIDAndStatus(facultyID, "PENDING");

        return ResponseEntity.ok(pendingRequests.stream().map(req -> Map.of(
                "requestID", req.getRequestID(),
                "studentName", req.getStudent().getName(),
                "studentRollNo", req.getStudent().getRollNo(),
                "tableName", req.getTableDetails().getTableName(),
                "status", req.getStatus()
        )));
    }

    @GetMapping("/request/{requestID}")
public ResponseEntity<Map<String, Object>> getRequestDetails(@PathVariable int requestID) {
    Optional<Request> requestOpt = requestRepository.findById(requestID);
    if (requestOpt.isEmpty()) {
        return ResponseEntity.status(404).body(Map.of("error", "Request not found"));
    }

    Request request = requestOpt.get();
    Map<String, Object> requestData = new HashMap<>();
    requestData.put("Student Name", request.getStudent().getName());
    requestData.put("Event", request.getTableDetails().getTableName());
    requestData.put("status", request.getStatus());

    // ✅ Fetch Additional Details Based on tableID & entryID
    int tableID = request.getTableDetails().getTableID();
    int entryID = request.getEntryID();

    if (tableID == 1) {  
        Optional<TechnicalDetail> techDetails = technicalDetailRepository.findById(entryID);
        techDetails.ifPresent(details -> {
            requestData.put("Event Name", details.getEvent().getName());
            requestData.put("Event Category", details.getEventCategory().getEventCategoryName());
            requestData.put("Event Date", details.getEventDate());
            requestData.put("Role", details.getRole());
            requestData.put("Achievement", details.getAchievement());
            requestData.put("AchievementDetails", details.getAchievementDetails());
            requestData.put("Other Details", details.getOtherDetails());
        });
    } 
    return ResponseEntity.ok(requestData);
}


   @PutMapping("/request/{requestID}")
    public ResponseEntity<String> updateRequestStatus(@PathVariable int requestID, @RequestBody Map<String, String> payload) {
        Optional<Request> requestOpt = requestRepository.findById(requestID);
        if (requestOpt.isEmpty()) return ResponseEntity.status(404).body("Request not found");

        Request request = requestOpt.get();
        String newStatus = payload.get("status");
        String facultyRemark = payload.get("remark");

        request.setStatus(newStatus);
        requestRepository.save(request);

        int tableID = request.getTableDetails().getTableID();
        int entryID = request.getEntryID();

        if (tableID == 1) {  
            Optional<TechnicalDetail> techDetailOpt = technicalDetailRepository.findById(entryID);
            techDetailOpt.ifPresent(techDetail -> {
                techDetail.setStatus(newStatus);
                techDetail.setRemark(facultyRemark);
                technicalDetailRepository.save(techDetail);
            });
        }

        return ResponseEntity.ok("Request " + newStatus + " with remarks saved.");
    }

    @GetMapping("/approved-requests/{facultyID}")
    public ResponseEntity<?> getApprovedRequests(@PathVariable int facultyID) {
    List<Request> approvedRequests = requestRepository.findByFacultyFacultyIDAndStatus(facultyID, "APPROVED");

    return ResponseEntity.ok(approvedRequests.stream().map(req -> Map.of(
            "requestID", req.getRequestID(),
            "studentName", req.getStudent().getName(),
            "studentRollNo", req.getStudent().getRollNo(),
            "tableName", req.getTableDetails().getTableName(),
            "status", req.getStatus()
    )));
}

@GetMapping("/rejected-requests/{facultyID}")
public ResponseEntity<?> getRejectedRequests(@PathVariable int facultyID) {
    List<Request> rejectedRequests = requestRepository.findByFacultyFacultyIDAndStatus(facultyID, "REJECTED");

    return ResponseEntity.ok(rejectedRequests.stream().map(req -> {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("requestID", req.getRequestID());
        requestData.put("studentName", req.getStudent().getName());
        requestData.put("studentRollNo", req.getStudent().getRollNo());
        requestData.put("tableName", req.getTableDetails().getTableName());
        requestData.put("status", req.getStatus());

        int tableID = req.getTableDetails().getTableID();
        int entryID = req.getEntryID();

        String facultyRemark = "No Remark";  

        if (tableID == 1) {  
            Optional<TechnicalDetail> techDetails = technicalDetailRepository.findById(entryID);
            facultyRemark = techDetails.map(TechnicalDetail::getRemark).orElse("No Remark");
        }

        requestData.put("remark", facultyRemark);
        return requestData;
    }).toList());
}


}
