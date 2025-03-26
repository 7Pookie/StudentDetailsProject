package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.TechnicalEvents;
import com.example.studentDetailsBackEnd.repository.TechnicalEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class EventController {

    @Autowired
    private TechnicalEventsRepository technicalEventsRepository; // ✅ Inject the repository

    // ✅ API to add a new event
    @PostMapping("/add")
    public ResponseEntity<TechnicalEvents> addEvent(@RequestBody TechnicalEvents event) {
        TechnicalEvents savedEvent = technicalEventsRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }
}
