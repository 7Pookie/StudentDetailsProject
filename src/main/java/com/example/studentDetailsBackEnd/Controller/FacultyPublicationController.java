package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.FacultyPublicationDTO;
import com.example.studentDetailsBackEnd.Model.FacultyPublication;
import com.example.studentDetailsBackEnd.Service.FacultyPublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty-publications")
public class FacultyPublicationController {

    @Autowired
    private FacultyPublicationService facultyPublicationService;

    @PostMapping("/add")
    public FacultyPublication addPublication(@RequestBody FacultyPublicationDTO dto) {
        return facultyPublicationService.savePublication(dto);
    }

    @GetMapping("/all")
    public List<FacultyPublication> getAllPublications() {
        return facultyPublicationService.getAllPublications();
    }
}
