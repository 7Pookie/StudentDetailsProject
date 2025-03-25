package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.EventCategory;
import com.example.studentDetailsBackEnd.repository.EventCategoryRepository;
import com.example.studentDetailsBackEnd.repository.SportCategoryRepository;
import com.example.studentDetailsBackEnd.Model.SportEventCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventCategories")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class EventCategoryController {

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private SportCategoryRepository sportCategoryRepository;

    @GetMapping
    public ResponseEntity<List<EventCategory>> getAllEventCategories() {
        return ResponseEntity.ok(eventCategoryRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEventCategory(@RequestBody EventCategory category) {
    if (category.getEventCategoryName() == null || category.getEventCategoryName().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("❌ Event category name cannot be empty.");
    }

    if (eventCategoryRepository.findByCategory(category.getEventCategoryName()) != null) {
        return ResponseEntity.badRequest().body("❌ Event category already exists.");
    }

    try {
        EventCategory savedCategory = eventCategoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("❌ Error saving category: " + e.getMessage());
    }
    }

    @PostMapping("/sport/add")
    public ResponseEntity<?> addEventCategory(@RequestBody SportEventCategory category) {
    if (category.getSportEventCategoryName() == null || category.getSportEventCategoryName().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("❌ Event category name cannot be empty.");
    }

    if (sportCategoryRepository.findByCategory(category.getSportEventCategoryName()) != null) {
        return ResponseEntity.badRequest().body("❌ Event category already exists.");
    }

    try {
        SportEventCategory savedCategory = sportCategoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("❌ Error saving category: " + e.getMessage());
    }    
}

}
