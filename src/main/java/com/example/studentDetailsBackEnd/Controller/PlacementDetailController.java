package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.PlacementDetailRequest;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/placements")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PlacementDetailController {

    @Autowired
    private PlacementDetailRepository placementDetailRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addPlacementDetail(@ModelAttribute PlacementDetailRequest request) {
        Optional<Student> studentOpt = studentRepository.findById(request.getStudentID());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Student not found!");
        }

        Student student = studentOpt.get();
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ No faculty assigned to student!");
        }

        PlacementCompanyDetail company;
        if (request.getCompanyID() != null) {
            company = companyRepository.findById(request.getCompanyID())
                    .orElse(null);
            if (company == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Selected company not found.");
            }
        } else if (request.getCustomCompanyName() != null && !request.getCustomCompanyName().trim().isEmpty()) {
            company = new PlacementCompanyDetail();
            company.setCompanyName(request.getCustomCompanyName().trim());
            company = companyRepository.save(company);
        } else {
            return ResponseEntity.badRequest().body("❌ Company must be selected or entered.");
        }

        PlacementDetail placementDetail = new PlacementDetail();
        placementDetail.setStudent(student);
        placementDetail.setCompany(company);
        placementDetail.setPlacementType(request.getPlacementType());
        placementDetail.setStartDate(request.getStartDate());
        placementDetail.setEndDate(request.getEndDate());
        placementDetail.setRole(request.getRole());
        placementDetail.setRemark(request.getRemark());
        placementDetail.setStatus("PENDING");

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            try {
                placementDetail.setOfferLetter(request.getFile().getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error saving file.");
            }
        }

        PlacementDetail savedDetail = placementDetailRepository.save(placementDetail);
        int entryID = savedDetail.getPlacementID();

        TableDetails tableDetails = tableDetailsRepository.findByTableName("placement_details");
        if (tableDetails == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Table entry for placement_details not found.");
        }

        Request newRequest = new Request();
        newRequest.setStudent(student);
        newRequest.setFaculty(faculty);
        newRequest.setTableDetails(tableDetails);
        newRequest.setEntryID(entryID);
        newRequest.setStatus("PENDING");

        requestRepository.save(newRequest);

        return ResponseEntity.ok("✅ Placement Detail added & Request sent for approval!");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPlacements() {
        return ResponseEntity.ok(placementDetailRepository.findAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, 
                                             @RequestParam String status, 
                                             @RequestParam String remark) {
        Optional<PlacementDetail> placementOpt = placementDetailRepository.findById(id);
        if (placementOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Placement Detail not found!");
        }

        PlacementDetail placement = placementOpt.get();
        placement.setStatus(status);
        placement.setRemark(remark);
        placementDetailRepository.save(placement);

        return ResponseEntity.ok("✅ Status updated successfully!");
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable int id) {
        Optional<PlacementDetail> placementOpt = placementDetailRepository.findById(id);
        if (placementOpt.isEmpty() || placementOpt.get().getOfferLetter() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=\"offer_letter.pdf\"")
                .body(placementOpt.get().getOfferLetter());
    }
}