package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.ClubDetail;
import com.example.studentDetailsBackEnd.Service.ClubDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/club-detail")
public class ClubDetailController {

    @Autowired
    private ClubDetailService clubDetailService;

    // ✅ Get all club details
    @GetMapping("/all")
    public ResponseEntity<List<ClubDetail>> getAllClubDetails() {
        return ResponseEntity.ok(clubDetailService.getAllClubDetails());
    }

    // ✅ Get a single club detail by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getClubDetailById(@PathVariable int id) {
        Optional<ClubDetail> clubDetail = clubDetailService.getClubDetailById(id);
        return clubDetail.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Club detail not found!"));
    }

    // ✅ Add a new club detail
    @PostMapping("/add")
    public ResponseEntity<ClubDetail> addClubDetail(@RequestBody ClubDetail clubDetail) {
        return ResponseEntity.ok(clubDetailService.addClubDetail(clubDetail));
    }

    // ✅ Update club details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClubDetail(@PathVariable int id, @RequestBody ClubDetail clubDetail) {
        clubDetailService.updateClubDetail(id, clubDetail);
        return ResponseEntity.ok("Club detail updated successfully!");
    }

    // ✅ Delete a club detail
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClubDetail(@PathVariable int id) {
        clubDetailService.deleteClubDetail(id);
        return ResponseEntity.ok("Club detail deleted successfully!");
    }
}