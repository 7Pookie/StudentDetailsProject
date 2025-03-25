package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.CulturalDetail;
import com.example.studentDetailsBackEnd.Model.ProfessionalSocietyDetail;
import com.example.studentDetailsBackEnd.Model.PlacementDetail;
import com.example.studentDetailsBackEnd.Service.StudentService;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.FacultyRepository;
import com.example.studentDetailsBackEnd.repository.ProfessionalSocietyDetailRepository;
import com.example.studentDetailsBackEnd.repository.TechnicalDetailRepository;  
import com.example.studentDetailsBackEnd.repository.CulturalDetailRepository;
import com.example.studentDetailsBackEnd.repository.PlacementDetailRepository;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.Model.SportDetail;
import com.example.studentDetailsBackEnd.Model.TechnicalDetail;
import com.example.studentDetailsBackEnd.repository.SportDetailRepository;
import com.example.studentDetailsBackEnd.repository.TechnicalDetailRepository;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.Model.TechnicalEvents;
import com.example.studentDetailsBackEnd.repository.EventCategoryRepository;
import com.example.studentDetailsBackEnd.Model.EventCategory;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/students")  
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StudentController {

    private final StudentService studentService;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private TechnicalDetailRepository technicalDetailRepository;

    @Autowired
    private CulturalDetailRepository culturalDetailRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private SportDetailRepository sportsDetailRepository;

    @Autowired
    private PlacementDetailRepository placementDetailRepository;

    @Autowired
    private ProfessionalSocietyDetailRepository professionalSocietyDetailRepository;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/details")
    public ResponseEntity<?> getStudentDetails(HttpSession session) {
        String email = (String) session.getAttribute("EMAIL");
        
        System.out.println("🔹 Session EMAIL: " + email); 
    
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

        session.setAttribute("EMAIL", email);
        
        Student student = studentOpt.get();
        System.out.println("✅ Found student: " + student.getStudentID());
        return ResponseEntity.ok(Map.of("studentID", student.getStudentID()));
    }

    @GetMapping("/profile/{studentID}")
    public ResponseEntity<?> getStudentProfile(@PathVariable int studentID) {
        try {
            Optional<Student> studentOpt = studentRepository.findById(studentID);

            if (studentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "❌ Student not found!"));
            }

            Student student = studentOpt.get();
            Faculty faculty = student.getFaculty();
            String facultyName = (faculty != null) ? faculty.getName() : "N/A";

            Map<String, Object> response = new HashMap<>();
            response.put("name", student.getName());
            response.put("dob", student.getDob() != null ? student.getDob().toString() : "Not Provided");
            response.put("email", student.getEmail());
            response.put("roll_no", student.getRollNo());
            response.put("branch", student.getBranch());
            response.put("program", student.getProgram());
            response.put("apaarid", student.getApaarId());
            response.put("facultyName", facultyName);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "❌ Internal Server Error!"));
        }
    }

    @GetMapping("/request/{requestID}")
    public ResponseEntity<?> getRequestDetails(@PathVariable int requestID) {
    try {
        Optional<Request> requestOpt = requestRepository.findById(requestID);

        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "❌ Request not found!"));
        }

        Request request = requestOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("status", request.getStatus());

        int tableID = request.getTableDetails().getTableID();
        int entryID = request.getEntryID();

        if (tableID == 1) { 
            Optional<TechnicalDetail> techOpt = technicalDetailRepository.findById(entryID);
            if (techOpt.isPresent()) {
                TechnicalDetail techDetail = techOpt.get();
                response.put("eventName", techDetail.getEvent().getName());
                response.put("role", techDetail.getRole());
                response.put("eventCategory", techDetail.getEventCategory().getEventCategoryName());
                response.put("eventDate", techDetail.getEventDate().toString());
                response.put("achievement", techDetail.getAchievement());
                response.put("achievementDetails", techDetail.getAchievementDetails());
                response.put("otherDetails", techDetail.getOtherDetails());
            }
        }

        else if(tableID==2){
            Optional<CulturalDetail> culturalOpt = culturalDetailRepository.findById(entryID);
            if (culturalOpt.isPresent()) {
                CulturalDetail culturalDetail = culturalOpt.get();
                response.put("eventName", culturalDetail.getEvent().getName());
                response.put("role", culturalDetail.getRole());
                response.put("eventCategory", culturalDetail.getEventCategory().getEventCategoryName());
                response.put("eventDate", culturalDetail.getEventDate().toString());
                response.put("achievement", culturalDetail.getAchievement());
                response.put("achievementDetails", culturalDetail.getAchievementDetails());
                response.put("otherDetails", culturalDetail.getOtherDetails());
            }   
        }

        else if(tableID==4){
            Optional<PlacementDetail> placementOpt = placementDetailRepository.findById(entryID);
            if(placementOpt.isPresent()){
                PlacementDetail placementDetail = placementOpt.get();
                response.put("placementType", placementDetail.getPlacementType());
                response.put("role", placementDetail.getRole());
                response.put("companyName", placementDetail.getCompany().getCompanyName());
                response.put("startDate", placementDetail.getStartDate().toString());
                response.put("endDate", placementDetail.getEndDate().toString());
        }
    }

        else if(tableID == 5){
            Optional<ProfessionalSocietyDetail> societyOpt = professionalSocietyDetailRepository.findById(entryID);
            if(societyOpt.isPresent()){
                ProfessionalSocietyDetail societyDetail = societyOpt.get();
                response.put("societyName", societyDetail.getSociety().getSocietyName());
                response.put("fieldName", societyDetail.getField().getFieldName());
                response.put("role", societyDetail.getRole());
                response.put("dateJoined", societyDetail.getDateJoined().toString());
                response.put("achievementDetails", societyDetail.getAchievementDetails());
        }
    }
        
        else if(tableID==6){
            Optional<SportDetail> sportsOpt = sportsDetailRepository.findById(entryID);
            if (sportsOpt.isPresent()) {
                SportDetail sportsDetail = sportsOpt.get();
                response.put("eventName", sportsDetail.getEvent().getSportEventName());
                response.put("role", sportsDetail.getRole());
                response.put("eventCategory", sportsDetail.getEventCategory().getSportEventCategoryName());
                response.put("eventDate", sportsDetail.getEventDate().toString());
                response.put("achievement", sportsDetail.getAchievement());
                response.put("achievementDetails", sportsDetail.getAchievementDetails());
                response.put("otherDetails", sportsDetail.getOtherDetails());
            }
        }

        return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "❌ Internal Server Error!"));
    }
}

    @GetMapping("/requests/{studentID}")
    public ResponseEntity<List<Map<String, Object>>> getStudentRequests(@PathVariable int studentID) {
        List<Request> requests = requestRepository.findByStudentStudentID(studentID);
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Request request : requests) {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("requestID", request.getRequestID());
            requestData.put("status", request.getStatus());

            int tableID = request.getTableDetails().getTableID();
            int entryID = request.getEntryID();

            if (tableID == 1) {  // ✅ Technical Events
                Optional<TechnicalDetail> techOpt = technicalDetailRepository.findById(entryID);
                if (techOpt.isPresent()) {
                    TechnicalDetail techDetail = techOpt.get();
                    requestData.put("eventName", techDetail.getEvent().getName());
                    requestData.put("role", techDetail.getRole());
                }
            }

            else if(tableID ==2){
                Optional<CulturalDetail> culturalOpt = culturalDetailRepository.findById(entryID);
                if (culturalOpt.isPresent()) {
                    CulturalDetail culturalDetail = culturalOpt.get();
                    requestData.put("eventName", culturalDetail.getEvent().getName());
                    requestData.put("role", culturalDetail.getRole());
                }
            }

            else if(tableID == 4){
                Optional<PlacementDetail> placementOpt = placementDetailRepository.findById(entryID);
                if(placementOpt.isPresent()){
                    PlacementDetail placementDetail = placementOpt.get();
                    requestData.put("role", placementDetail.getRole());
                    requestData.put("companyName", placementDetail.getCompany().getCompanyName());
                }
            }

            else if( tableID ==5){
                Optional<ProfessionalSocietyDetail> societyOpt = professionalSocietyDetailRepository.findById(entryID);
                if(societyOpt.isPresent()){
                    ProfessionalSocietyDetail societyDetail = societyOpt.get();
                    requestData.put("role", societyDetail.getRole());
                    requestData.put("societyName", societyDetail.getSociety().getSocietyName());
                }
            }

            else if(tableID==6){
                Optional<SportDetail> sportsOpt = sportsDetailRepository.findById(entryID);
                if (sportsOpt.isPresent()) {
                    SportDetail sportsDetail = sportsOpt.get();
                    requestData.put("eventName", sportsDetail.getEvent().getSportEventName());
                    requestData.put("role", sportsDetail.getRole());
                }
            }

            responseList.add(requestData);
        }

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/request/{requestID}/resubmit")
    public ResponseEntity<String> resubmitRequest(@PathVariable int requestID, @RequestBody Map<String, String> payload) {
        Optional<Request> requestOpt = requestRepository.findById(requestID);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Request not found");
        }

        Request request = requestOpt.get();
        int tableID = request.getTableDetails().getTableID();
        int entryID = request.getEntryID();
        boolean updated = false;

        if (tableID == 1) {
            Optional<TechnicalDetail> techDetailsOpt = technicalDetailRepository.findById(entryID);
            if (techDetailsOpt.isPresent()) {
                TechnicalDetail techDetails = techDetailsOpt.get();
                if (payload.containsKey("eventName")) {
                    techDetails.getEvent().setName(payload.get("eventName"));
                    updated = true;
                }
                if (payload.containsKey("role")) {
                    techDetails.setRole(payload.get("role"));
                    updated = true;
                }
                technicalDetailRepository.save(techDetails);
            }
        }
        else if(tableID ==2){
            Optional<CulturalDetail> culturalDetailsOpt = culturalDetailRepository.findById(entryID);
            if (culturalDetailsOpt.isPresent()) {
                CulturalDetail culturalDetails = culturalDetailsOpt.get();
                if (payload.containsKey("eventName")) {
                    culturalDetails.getEvent().setName(payload.get("eventName"));
                    updated = true;
                }
                if (payload.containsKey("role")) {
                    culturalDetails.setRole(payload.get("role"));
                    updated = true;
                }
                culturalDetailRepository.save(culturalDetails);
            }
        }

        else if(tableID == 4){
            Optional<PlacementDetail> placementDetailsOpt = placementDetailRepository.findById(entryID);
            if (placementDetailsOpt.isPresent()) {
                PlacementDetail placementDetails = placementDetailsOpt.get();
                if (payload.containsKey("companyName")) {
                    placementDetails.getCompany().setCompanyName(payload.get("eventName"));
                    updated = true;
                }
                if (payload.containsKey("role")) {
                    placementDetails.setRole(payload.get("role"));
                    updated = true;
                }
                placementDetailRepository.save(placementDetails);
            }
        }

        else if(tableID ==5){
            Optional<ProfessionalSocietyDetail> societyOpt = professionalSocietyDetailRepository.findById(entryID);
            if (societyOpt.isPresent()) {
                ProfessionalSocietyDetail societyDetails = societyOpt.get();
                if (payload.containsKey("societyName")) {
                    societyDetails.getSociety().setSocietyName(payload.get("societyName"));
                    updated = true;
                }
                if (payload.containsKey("role")) {
                    societyDetails.setRole(payload.get("role"));
                    updated = true;
                }
                professionalSocietyDetailRepository.save(societyDetails);
            }
        }

        else if(tableID==6){
            Optional<SportDetail> sportsDetailsOpt = sportsDetailRepository.findById(entryID);
            if (sportsDetailsOpt.isPresent()) {
                SportDetail sportsDetails = sportsDetailsOpt.get();
                if (payload.containsKey("eventName")) {
                    sportsDetails.getEvent().setSportEventName(payload.get("eventName"));
                    updated = true;
                }
                if (payload.containsKey("role")) {
                    sportsDetails.setRole(payload.get("role"));
                    updated = true;
                }
                sportsDetailRepository.save(sportsDetails);
            }
        }

        if (!updated) {
            return ResponseEntity.status(404).body("❌ No updates made.");
        }

        request.setStatus("PENDING");
        requestRepository.save(request);
        return ResponseEntity.ok("✅ Request updated and resubmitted successfully!");
    }
}
