package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.Model.TechnicalDetail;
import com.example.studentDetailsBackEnd.repository.TechnicalDetailRepository;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.repository.CulturalDetailRepository;
import com.example.studentDetailsBackEnd.repository.ProfessionalSocietyDetailRepository;
import com.example.studentDetailsBackEnd.repository.StudentPublicationRepository;

import com.example.studentDetailsBackEnd.repository.SportDetailRepository;
import com.example.studentDetailsBackEnd.repository.PlacementDetailRepository;
import com.example.studentDetailsBackEnd.Model.PlacementDetail;
import com.example.studentDetailsBackEnd.Model.ProfessionalSocietyDetail;
import com.example.studentDetailsBackEnd.Model.StudentPublication;

import com.example.studentDetailsBackEnd.Model.CulturalDetail;
import com.example.studentDetailsBackEnd.Model.SportDetail;

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

    @Autowired
    private CulturalDetailRepository culturalDetailRepository;

    @Autowired
    private SportDetailRepository sportDetailRepository;

    @Autowired
    private PlacementDetailRepository placementDetailRepository;

    @Autowired
    private ProfessionalSocietyDetailRepository professionalSocietyDetailRepository;

    @Autowired
    private StudentPublicationRepository studentPublicationRepository;

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

            if (details.getOfferLetter() != null) {
                String fileUrl = "http://localhost:8080/api/technical-details/" + details.getID() + "/file";
                requestData.put("Offer Letter", fileUrl);
            } else {
                requestData.put("Offer Letter", "No file uploaded");
            }
        });
    } 

    else if(tableID == 2) {
        Optional<CulturalDetail> culturalDetails = culturalDetailRepository.findById(entryID);
        culturalDetails.ifPresent(details -> {
            requestData.put("Event Name", details.getEvent().getName());
            requestData.put("Event Category", details.getEventCategory().getEventCategoryName());
            requestData.put("Event Date", details.getEventDate());
            requestData.put("Role", details.getRole());
            requestData.put("Achievement", details.getAchievement());
            requestData.put("AchievementDetails", details.getAchievementDetails());
            requestData.put("Other Details", details.getOtherDetails());

            if (details.getOfferLetter() != null) {
                String fileUrl = "http://localhost:8080/api/cultural-details/" + details.getID() + "/file";
                requestData.put("Offer Letter", fileUrl);
            } else {
                requestData.put("Offer Letter", "No file uploaded");
            }
        });
    }

    else if(tableID == 4){
        Optional<PlacementDetail> placementDetails = placementDetailRepository.findById(entryID);
        placementDetails.ifPresent(details -> {
            requestData.put("Placement Type", details.getPlacementType());
            requestData.put("Company", details.getCompany().getCompanyName());
            requestData.put("Start Date", details.getStartDate());
            requestData.put("End Date", details.getEndDate());
            requestData.put("Role", details.getRole());

            if (details.getOfferLetter() != null) {
                String fileUrl = "http://localhost:8080/api/placements/" + details.getPlacementID() + "/file";
                requestData.put("Offer Letter", fileUrl);
            } else {
                requestData.put("Offer Letter", "No file uploaded");
            }
        });
    }

    else if(tableID == 5){
        Optional<ProfessionalSocietyDetail> societyDetails = professionalSocietyDetailRepository.findById(entryID);
        societyDetails.ifPresent(details -> {
            requestData.put("Society Name", details.getSociety().getSocietyName());
            requestData.put("Field", details.getField().getFieldName());
            requestData.put("Date Joined", details.getDateJoined());
            requestData.put("Role", details.getRole());
            requestData.put("Achievement",details.getAchievementDetails());

            if (details.getOfferLetter() != null) {
                String fileUrl = "http://localhost:8080/api/professional-society/" + details.getSocietyDetailsID() + "/file";
                requestData.put("Offer Letter", fileUrl);
            } else {
                requestData.put("Offer Letter", "No file uploaded");
            }
        });
    }
    

    else if(tableID == 6){
        Optional<SportDetail> sportDetails = sportDetailRepository.findById(entryID);
        sportDetails.ifPresent(details -> {
            requestData.put("Event Name", details.getEvent().getSportEventName());
            requestData.put("Event Category", details.getEventCategory().getSportEventCategoryName());
            requestData.put("Event Date", details.getEventDate());
            requestData.put("Role", details.getRole());
            requestData.put("Achievement", details.getAchievement());
            requestData.put("AchievementDetails", details.getAchievementDetails());
            requestData.put("Other Details", details.getOtherDetails());

            if (details.getOfferLetter() != null) {
                String fileUrl = "http://localhost:8080/api/sport-details/" + details.getSportEventDetailsId() + "/file";
                requestData.put("Offer Letter", fileUrl);
            } else {
                requestData.put("Offer Letter", "No file uploaded");
            }
        });
    }

    else if(tableID ==7){
        Optional<StudentPublication> pubDetails = studentPublicationRepository.findById(entryID);
        pubDetails.ifPresent(details -> {
            requestData.put("Publication Title", details.getTitle());
            requestData.put("Type", details.getType());
            requestData.put("Publication Date", details.getPublicationDate());
            requestData.put("Authors", details.getAuthors());
            requestData.put("ISBN/ISSN", details.getNumber());
            requestData.put("Publication Status", details.getPublicationStatus());

            if (details.getOfferLetter() != null) {
                String fileUrl = "http://localhost:8080/api/student-publications/" + details.getStudentPubID() + "/file";
                requestData.put("Offer Letter", fileUrl);
            } else {
                requestData.put("Offer Letter", "No file uploaded");
            }
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

        else if(tableID==2) {
            Optional<CulturalDetail> culturalDetailOpt = culturalDetailRepository.findById(entryID);
            culturalDetailOpt.ifPresent(culturalDetail -> {
                culturalDetail.setStatus(newStatus);
                culturalDetail.setRemark(facultyRemark);
                culturalDetailRepository.save(culturalDetail);
            });
        }

        else if(tableID==4) {
            Optional<PlacementDetail> placementDetailOpt = placementDetailRepository.findById(entryID);
            placementDetailOpt.ifPresent(placementDetail -> {
                placementDetail.setStatus(newStatus);
                placementDetail.setRemark(facultyRemark);
                placementDetailRepository.save(placementDetail);
            });
        }

        else if(tableID==5) {
            Optional<ProfessionalSocietyDetail> societyOpt = professionalSocietyDetailRepository.findById(entryID);
            societyOpt.ifPresent(societyDetail -> {
                societyDetail.setStatus(newStatus);
                societyDetail.setRemark(facultyRemark);
                professionalSocietyDetailRepository.save(societyDetail);
            });
        }

        else if(tableID==6) {
            Optional<SportDetail> sportDetailOpt = sportDetailRepository.findById(entryID);
            sportDetailOpt.ifPresent(sportDetail -> {
                sportDetail.setStatus(newStatus);
                sportDetail.setRemark(facultyRemark);
                sportDetailRepository.save(sportDetail);
            });
        }

        else if(tableID==7) {
            Optional<StudentPublication> pubOpt = studentPublicationRepository.findById(entryID);
            pubOpt.ifPresent(pubDetail -> {
                pubDetail.setStatus(newStatus);
                pubDetail.setRemark(facultyRemark);
                studentPublicationRepository.save(pubDetail);
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
        else if(tableID==2)
        {
            Optional<CulturalDetail> culturalDetails = culturalDetailRepository.findById(entryID);
            facultyRemark = culturalDetails.map(CulturalDetail::getRemark).orElse("No Remark");
        }
        else if(tableID==4)
        {
            Optional<PlacementDetail> placementDetails = placementDetailRepository.findById(entryID);
            facultyRemark = placementDetails.map(PlacementDetail::getRemark).orElse("No Remark");
        }
        else if(tableID==5)
        {
            Optional<ProfessionalSocietyDetail> societyDetails = professionalSocietyDetailRepository.findById(entryID);
            facultyRemark = societyDetails.map(ProfessionalSocietyDetail::getRemark).orElse("No Remark");
        }
        else if(tableID==6)
        {
            Optional<SportDetail> sportDetails = sportDetailRepository.findById(entryID);
            facultyRemark = sportDetails.map(SportDetail::getRemark).orElse("No Remark");
        }

        else if(tableID==7)
        {
            Optional<StudentPublication> pubDetails = studentPublicationRepository.findById(entryID);
            facultyRemark = pubDetails.map(StudentPublication::getRemark).orElse("No Remark");
        }

        requestData.put("remark", facultyRemark);
        return requestData;
    }).toList());
}


}
