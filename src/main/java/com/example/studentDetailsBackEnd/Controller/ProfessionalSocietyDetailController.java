package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.ProfessionalSocietyDetailRequest;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professional-society-details")
public class ProfessionalSocietyDetailController {

    private final ProfessionalSocietyDetailRepository societyDetailRepository;
    private final ProfessionalSocietyRepository societyRepository;
    private final ProfessionalSocietyFieldRepository fieldRepository;
    private final StudentRepository studentRepository;
    private final TableDetailsRepository tableDetailsRepository;
    private final RequestRepository requestRepository;

    public ProfessionalSocietyDetailController(
            ProfessionalSocietyDetailRepository societyDetailRepository,
            ProfessionalSocietyRepository societyRepository,
            ProfessionalSocietyFieldRepository fieldRepository,
            StudentRepository studentRepository,
            TableDetailsRepository tableDetailsRepository,
            RequestRepository requestRepository) {
        this.societyDetailRepository = societyDetailRepository;
        this.societyRepository = societyRepository;
        this.fieldRepository = fieldRepository;
        this.studentRepository = studentRepository;
        this.tableDetailsRepository = tableDetailsRepository;
        this.requestRepository = requestRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProfessionalSocietyDetail(@ModelAttribute ProfessionalSocietyDetailRequest request) throws IOException {
        if (request.getFile() == null || request.getFile().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Please upload the certificate!");
        }

        Optional<Student> studentOpt = studentRepository.findById(request.getStudentID());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Student not found!");
        }
        Student student = studentOpt.get();

        if (student.getFaculty() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No faculty assigned to student!");
        }

        ProfessionalSociety society;
        if (request.getSocietyID() != null) {
            Optional<ProfessionalSociety> societyOpt = societyRepository.findById(request.getSocietyID());
            if (societyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Society must be selected or entered.");
            }
            society = societyOpt.get();
        } else if (request.getCustomSocietyName() != null && !request.getCustomSocietyName().trim().isEmpty()) {
            society = new ProfessionalSociety();
            society.setSocietyName(request.getCustomSocietyName());
            society = societyRepository.save(society);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Society must be selected or entered.");
        }

        ProfessionalSocietyField field;
        if (request.getFieldID() != null) {
            Optional<ProfessionalSocietyField> fieldOpt = fieldRepository.findById(request.getFieldID());
            if (fieldOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Field must be selected or entered.");
            }
            field = fieldOpt.get();
        } else if (request.getCustomFieldName() != null && !request.getCustomFieldName().trim().isEmpty()) {
            field = new ProfessionalSocietyField();
            field.setFieldName(request.getCustomFieldName());
            field = fieldRepository.save(field);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Field must be selected or entered.");
        }

        ProfessionalSocietyDetail detail = new ProfessionalSocietyDetail();
        detail.setStudent(student);
        detail.setSociety(society);
        detail.setField(field);
        detail.setDateJoined(request.getDateJoined());
        detail.setRole(request.getRole());
        detail.setAchievementDetails(request.getAchievementDetails());
        detail.setOfferLetter(request.getFile().getBytes());
        detail.setStatus("PENDING");
        societyDetailRepository.save(detail);

        TableDetails tableDetails = tableDetailsRepository.findByTableName("professional_society_details");
        Request req = new Request();
        req.setStudent(student);
        req.setFaculty(student.getFaculty());
        req.setTableDetails(tableDetails);
        req.setEntryID(detail.getSocietyDetailsID());
        req.setStatus("PENDING");
        requestRepository.save(req);

        return ResponseEntity.ok("✅ Professional Society Detail added & Request sent for approval!");
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable int id) {
        Optional<ProfessionalSocietyDetail> detailOpt = societyDetailRepository.findById(id);
        if (detailOpt.isEmpty() || detailOpt.get().getOfferLetter() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(detailOpt.get().getOfferLetter());
    }

    @GetMapping("/allsocieties")
    public ResponseEntity<List<ProfessionalSociety>> getAllSocieties() {
        return ResponseEntity.ok(societyRepository.findAll());
    }

    @PostMapping("/addSociety")
    public ResponseEntity<ProfessionalSociety> addSociety(@RequestBody ProfessionalSociety society) {
        return ResponseEntity.ok(societyRepository.save(society));
    }

    @GetMapping("/allfields")
    public ResponseEntity<List<ProfessionalSocietyField>> getAllFields() {
        return ResponseEntity.ok(fieldRepository.findAll());
    }

    @PostMapping("/addField")
    public ResponseEntity<ProfessionalSocietyField> addField(@RequestBody ProfessionalSocietyField field) {
        return ResponseEntity.ok(fieldRepository.save(field));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProfessionalSocietyDetails() {
        return ResponseEntity.ok(societyDetailRepository.findAll());
    }

    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @PathVariable String status) {
        Optional<ProfessionalSocietyDetail> detailOpt = societyDetailRepository.findById(id);
        if (detailOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Society Detail not found!");
        }
        ProfessionalSocietyDetail detail = detailOpt.get();
        detail.setStatus(status);
        societyDetailRepository.save(detail);
        return ResponseEntity.ok("✅ Status updated successfully!");
    }
}