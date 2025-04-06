package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.Service.RequestService;
import com.example.studentDetailsBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RequestController {

    @Autowired private RequestService requestService;
    @Autowired private StudentRepository studentRepository;
    @Autowired private FacultyRepository facultyRepository;
    @Autowired private TableDetailsRepository tableDetailsRepository;
    
    // Repositories for various tables
    @Autowired private StudentPublicationRepository studentPublicationRepository;
    @Autowired private TechnicalDetailRepository technicalDetailRepository;
    @Autowired private CulturalDetailRepository culturalDetailRepository;
    @Autowired private PlacementDetailRepository placementDetailRepository;
    @Autowired private ProfessionalSocietyDetailRepository professionalSocietyDetailRepository;
    @Autowired private SportDetailRepository sportDetailRepository;

    @PutMapping("/{requestID}/resubmit")
    public ResponseEntity<?> resubmitRequest(@PathVariable int requestID, @RequestBody Map<String, Object> updatedData) {
        System.out.println("📌 Resubmitting request with ID: " + requestID);
        
        Optional<Request> requestOpt = requestService.getRequestById(requestID);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Request not found!");
        }

        Request request = requestOpt.get();
        try {
            boolean updated = updateOriginalEntry(request, updatedData);
            if (!updated) {
                return ResponseEntity.badRequest().body("❌ Failed to update original entry!");
            }

            // Update request status
            request.setStatus("RESUBMITTED");
            requestService.saveRequest(request);
            
            System.out.println("✅ Request resubmitted successfully!");
            return ResponseEntity.ok("✅ Request resubmitted successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error resubmitting request: " + e.getMessage());
            return ResponseEntity.status(500).body("❌ Error resubmitting request: " + e.getMessage());
        }
    }

    private boolean updateOriginalEntry(Request request, Map<String, Object> updatedData) {
        String tableName = request.getTableDetails().getTableName();
        int entryId = request.getEntryID();

        System.out.println("🔄 Updating table: " + tableName + " for entryID: " + entryId);

        return switch (tableName) {
            case "student_publications" -> updateStudentPublication(entryId, updatedData);
            case "technical_event_details" -> updateTechnicalDetail(entryId, updatedData);
            case "cultural_event_details" -> updateCulturalDetail(entryId, updatedData);
            case "placement_details" -> updatePlacementDetail(entryId, updatedData);
            case "professional_society_details" -> updateProfessionalSocietyDetail(entryId, updatedData);
            case "sport_details" -> updateSportDetail(entryId, updatedData);
            default -> {
                System.err.println("❌ Unknown table: " + tableName);
                throw new IllegalArgumentException("Unknown table: " + tableName);
            }
        };
    }

    private boolean updateStudentPublication(int entryId, Map<String, Object> updatedData) {
        Optional<StudentPublication> publicationOpt = studentPublicationRepository.findById(entryId);
        if (publicationOpt.isEmpty()) return false;

        StudentPublication publication = publicationOpt.get();
        setIfPresent(updatedData, "title", publication::setTitle);
        setIfPresent(updatedData, "authors", publication::setAuthors);
        setIfPresent(updatedData, "type", publication::setType);
        setIfPresent(updatedData, "publicationStatus", publication::setPublicationStatus);
        setIfPresent(updatedData, "number", publication::setNumber);
        setIfPresentAsDate(updatedData, "publicationDate", publication::setPublicationDate);

        publication.setStatus("RESUBMITTED");
        studentPublicationRepository.save(publication);
        return true;
    }

    private boolean updateTechnicalDetail(int entryId, Map<String, Object> updatedData) {
        Optional<TechnicalDetail> detailOpt = technicalDetailRepository.findById(entryId);
        if (detailOpt.isEmpty()) return false;

        TechnicalDetail detail = detailOpt.get();
        setIfPresent(updatedData, "role", detail::setRole);
        setIfPresent(updatedData, "achievement", detail::setAchievement);
        setIfPresent(updatedData, "achievementDetails", detail::setAchievementDetails);
        setIfPresent(updatedData, "otherDetails", detail::setOtherDetails);
        setIfPresentAsDate(updatedData, "eventDate", detail::setEventDate);

        detail.setStatus("RESUBMITTED");
        technicalDetailRepository.save(detail);
        return true;
    }

    private boolean updateCulturalDetail(int entryId, Map<String, Object> updatedData) {
        Optional<CulturalDetail> detailOpt = culturalDetailRepository.findById(entryId);
        if (detailOpt.isEmpty()) return false;

        CulturalDetail detail = detailOpt.get();
        setIfPresent(updatedData, "role", detail::setRole);
        setIfPresent(updatedData, "achievement", detail::setAchievement);
        setIfPresent(updatedData, "achievementDetails", detail::setAchievementDetails);
        setIfPresent(updatedData, "otherDetails", detail::setOtherDetails);
        setIfPresentAsDate(updatedData, "eventDate", detail::setEventDate);

        detail.setStatus("RESUBMITTED");
        culturalDetailRepository.save(detail);
        return true;
    }

    private boolean updatePlacementDetail(int entryId, Map<String, Object> updatedData) {
        Optional<PlacementDetail> detailOpt = placementDetailRepository.findById(entryId);
        if (detailOpt.isEmpty()) return false;

        PlacementDetail detail = detailOpt.get();
        setIfPresent(updatedData, "placementType", detail::setPlacementType);
        setIfPresent(updatedData, "role", detail::setRole);
        setIfPresentAsDate(updatedData, "startDate", detail::setStartDate);
        setIfPresentAsDate(updatedData, "endDate", detail::setEndDate);

        detail.setStatus("RESUBMITTED");
        placementDetailRepository.save(detail);
        return true;
    }

    private boolean updateProfessionalSocietyDetail(int entryId, Map<String, Object> updatedData) {
        Optional<ProfessionalSocietyDetail> detailOpt = professionalSocietyDetailRepository.findById(entryId);
        if (detailOpt.isEmpty()) return false;

        ProfessionalSocietyDetail detail = detailOpt.get();
        setIfPresent(updatedData, "role", detail::setRole);
        setIfPresent(updatedData, "achievementDetails", detail::setAchievementDetails);
        setIfPresentAsDate(updatedData, "dateJoined", detail::setDateJoined);

        detail.setStatus("RESUBMITTED");
        professionalSocietyDetailRepository.save(detail);
        return true;
    }

    private boolean updateSportDetail(int entryId, Map<String, Object> updatedData) {
        Optional<SportDetail> detailOpt = sportDetailRepository.findById(entryId);
        if (detailOpt.isEmpty()) return false;

        SportDetail detail = detailOpt.get();
        setIfPresent(updatedData, "role", detail::setRole);
        setIfPresent(updatedData, "achievement", detail::setAchievement);
        setIfPresent(updatedData, "achievementDetails", detail::setAchievementDetails);
        setIfPresent(updatedData, "otherDetails", detail::setOtherDetails);
        setIfPresentAsDate(updatedData, "eventDate", detail::setEventDate);

        detail.setStatus("RESUBMITTED");
        sportDetailRepository.save(detail);
        return true;
    }

    @GetMapping("/faculty/{facultyID}")
    public ResponseEntity<List<Request>> getRequestsForFaculty(@PathVariable int facultyID) {
        return ResponseEntity.ok(requestService.getRequestsForFaculty(facultyID));
    }

    @GetMapping("/{requestID}")
public ResponseEntity<?> getRequestById(@PathVariable int requestID) {
    Optional<Request> requestOpt = requestService.getRequestById(requestID);
    
    if (requestOpt.isPresent()) {
        return ResponseEntity.ok(requestOpt.get());
    } else {
        return ResponseEntity.status(404).body("❌ Request not found!");
    }
}


    private void setIfPresent(Map<String, Object> map, String key, java.util.function.Consumer<String> setter) {
        if (map.containsKey(key)) setter.accept(map.get(key).toString());
    }

    private void setIfPresentAsDate(Map<String, Object> map, String key, java.util.function.Consumer<LocalDate> setter) {
        if (map.containsKey(key)) setter.accept(LocalDate.parse(map.get(key).toString()));
    }
}
