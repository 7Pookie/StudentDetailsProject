package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.Model.TechnicalEvents;
import com.example.studentDetailsBackEnd.repository.TechnicalEventsRepository;
import com.example.studentDetailsBackEnd.Model.CulturalEvents;
import com.example.studentDetailsBackEnd.repository.CulturalEventsRepository;
import com.example.studentDetailsBackEnd.Model.SportEvents;
import com.example.studentDetailsBackEnd.repository.SportEventsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class EventController {

    @Autowired
    private TechnicalEventsRepository technicalEventsRepository;
    
    @Autowired
    private CulturalEventsRepository culturalEventsRepository;

    @Autowired
    private SportEventsRepository sportEventsRepository;

    @PostMapping("/technical/add")
    public ResponseEntity<TechnicalEvents> addEvent(@RequestBody TechnicalEvents event) {
        TechnicalEvents savedEvent = technicalEventsRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    @PostMapping("/cultural/add")
    public ResponseEntity<CulturalEvents> addEvent(@RequestBody CulturalEvents event) {
        CulturalEvents savedEvent = culturalEventsRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    @PostMapping("/sport/add")
    public ResponseEntity<SportEvents> addEvent(@RequestBody SportEvents event) {
        SportEvents savedEvent = sportEventsRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }
}
