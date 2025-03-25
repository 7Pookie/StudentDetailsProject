package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.ProfessionalSocietyDetailRequest;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/professional-society")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProfessionalSocietyDetailController {

    @Autowired
    private ProfessionalSocietyDetailRepository professionalSocietyDetailRepository;

    @Autowired
    private ProfessionalSocietyRepository professionalSocietyRepository;

    @Autowired
    private ProfessionalSocietyFieldRepository professionalSocietyFieldRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    @Autowired
    private RequestRepository requestRepository;

    /**
     * ✅ Fetch all professional societies
     */
    @GetMapping("/societies")
    public ResponseEntity<List<ProfessionalSociety>> getAllSocieties() {
        return ResponseEntity.ok(professionalSocietyRepository.findAll());
    }

    /**
     * ✅ Add a new professional society
     */
    @PostMapping("/societies/add")
    public ResponseEntity<ProfessionalSociety> addSociety(@RequestBody ProfessionalSociety society) {
        return ResponseEntity.ok(professionalSocietyRepository.save(society));
    }

    /**
     * ✅ Fetch all professional society fields
     */
    @GetMapping("/fields")
    public ResponseEntity<List<ProfessionalSocietyField>> getAllFields() {
        return ResponseEntity.ok(professionalSocietyFieldRepository.findAll());
    }

    /**
     * ✅ Add a new professional society field
     */
    @PostMapping("/fields/add")
    public ResponseEntity<ProfessionalSocietyField> addField(@RequestBody ProfessionalSocietyField field) {
        return ResponseEntity.ok(professionalSocietyFieldRepository.save(field));
    }

    /**
     * ✅ Add a new Professional Society Detail entry
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProfessionalSocietyDetail(@RequestBody ProfessionalSocietyDetailRequest request) {
        Optional<Student> studentOpt = studentRepository.findById(request.getStudentID());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Student not found!");
        }
        Student student = studentOpt.get();
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No faculty assigned to student!");
        }

        // ✅ Handle Society Selection / Creation
        ProfessionalSociety society;
        if (request.getSocietyID() != null) {
            society = professionalSocietyRepository.findById(request.getSocietyID())
                    .orElseThrow(() -> new RuntimeException("❌ Society not found!"));
        } else if (request.getCustomSocietyName() != null && !request.getCustomSocietyName().trim().isEmpty()) {
            society = new ProfessionalSociety();
            society.setSocietyName(request.getCustomSocietyName());
            society = professionalSocietyRepository.save(society);
        } else {
            return ResponseEntity.badRequest().body("❌ Society must be selected or entered.");
        }

        // ✅ Handle Field Selection / Creation
        ProfessionalSocietyField field;
        if (request.getFieldID() != null) {
            field = professionalSocietyFieldRepository.findById(request.getFieldID())
                    .orElseThrow(() -> new RuntimeException("❌ Field not found!"));
        } else if (request.getCustomFieldName() != null && !request.getCustomFieldName().trim().isEmpty()) {
            field = new ProfessionalSocietyField();
            field.setFieldName(request.getCustomFieldName());
            field = professionalSocietyFieldRepository.save(field);
        } else {
            return ResponseEntity.badRequest().body("❌ Field must be selected or entered.");
        }

        // ✅ Create Professional Society Detail Entry
        ProfessionalSocietyDetail societyDetail = new ProfessionalSocietyDetail();
        societyDetail.setStudent(student);
        societyDetail.setSociety(society);
        societyDetail.setField(field);
        societyDetail.setDateJoined(request.getDateJoined());
        societyDetail.setRole(request.getRole());
        societyDetail.setAchievementDetails(request.getAchievementDetails());
        societyDetail.setStatus("PENDING");

        ProfessionalSocietyDetail savedDetail = professionalSocietyDetailRepository.save(societyDetail);
        int entryID = savedDetail.getSocietyDetailsID();

        // ✅ Fetch Table ID for "professional_society_details"
        Optional<Integer> tableIDOpt = Optional.ofNullable(tableDetailsRepository.findByTableName("professional_society_details"))
                .map(TableDetails::getTableID);
        if (tableIDOpt.isEmpty()) {
            return ResponseEntity.status(500).body("Table entry for professional_society_details not found.");
        }
        int tableID = tableIDOpt.get();

        // ✅ Create a Request Entry for Faculty Approval
        Request newRequest = new Request();
        newRequest.setStudent(student);
        newRequest.setFaculty(faculty);
        newRequest.setTableDetails(tableDetailsRepository.findById(tableID).get());
        newRequest.setEntryID(entryID);
        newRequest.setStatus("PENDING");

        requestRepository.save(newRequest);
        return ResponseEntity.ok("✅ Professional Society Detail added & Request sent for approval!");
    }

    /**
     * ✅ Fetch all Professional Society Details
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllProfessionalSocietyDetails() {
        return ResponseEntity.ok(professionalSocietyDetailRepository.findAll());
    }

    /**
     * ✅ Update Status of a Professional Society Detail
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String status) {
        Optional<ProfessionalSocietyDetail> societyDetailOpt = professionalSocietyDetailRepository.findById(id);
        if (societyDetailOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Society Detail not found!");
        }

        ProfessionalSocietyDetail societyDetail = societyDetailOpt.get();
        societyDetail.setStatus(status);
        professionalSocietyDetailRepository.save(societyDetail);

        return ResponseEntity.ok("✅ Status updated successfully!");
    }
}
