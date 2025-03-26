package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.EventCategory;
import com.example.studentDetailsBackEnd.repository.EventCategoryRepository;
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

    // ✅ Fetch all event categories
    @GetMapping
    public ResponseEntity<List<EventCategory>> getAllEventCategories() {
        return ResponseEntity.ok(eventCategoryRepository.findAll());
    }

    // ✅ Add a new event category (only if it does not exist)
    @PostMapping("/add")
    public ResponseEntity<?> addEventCategory(@RequestBody EventCategory category) {
        if (eventCategoryRepository.findByCategory(category.getEventCategoryName()) != null) {
            return ResponseEntity.badRequest().body("Event category already exists.");
        }
        return ResponseEntity.ok(eventCategoryRepository.save(category));
    }
}
