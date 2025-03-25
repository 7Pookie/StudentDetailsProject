package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.PlacementCompanyDetail;
import com.example.studentDetailsBackEnd.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // ✅ Fetch all companies
    @GetMapping("/all")
    public ResponseEntity<List<PlacementCompanyDetail>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCompany(@RequestBody PlacementCompanyDetail company) {
        if (company.getCompanyName() == null || company.getCompanyName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Company name cannot be empty!");
        }
        return ResponseEntity.ok(companyService.addCompany(company.getCompanyName()));
    }
}
