package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.CulturalDetailRequest;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.Service.CulturalDetailService;
import com.example.studentDetailsBackEnd.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CulturalDetailControllerTest {

    @Mock
    private CulturalDetailService culturalDetailService;
    @Mock
    private CulturalDetailRepository culturalDetailRepository;
    @Mock
    private CulturalEventsRepository culturalEventsRepository;
    @Mock
    private EventCategoryRepository eventCategoryRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TableDetailsRepository tableDetailsRepository;
    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private CulturalDetailController culturalDetailController;

    private CulturalDetailRequest request;
    private Student student;
    private CulturalEvents event;
    private EventCategory eventCategory;
    private CulturalDetail culturalDetail;

    @BeforeEach
    public void setUp() {
        Faculty faculty = new Faculty();
        faculty.setFacultyId(1);

        student = new Student();
        student.setStudentId(1);
        student.setFaculty(faculty);

        event = new CulturalEvents();
        event.setEventId(1);
        event.setName("Dance Fest");

        eventCategory = new EventCategory();
        eventCategory.setEventCategoryId(1);
        eventCategory.setEventCategoryName("Dance");

        culturalDetail = new CulturalDetail();
        culturalDetail.setCulturalDetailId(1);
        culturalDetail.setStudent(student);
        culturalDetail.setEvent(event);
        culturalDetail.setEventCategory(eventCategory);
        culturalDetail.setStatus("PENDING");

        request = new CulturalDetailRequest();
        request.setStudentID(1);
        request.setEventID(1);
        request.setEventCategoryID(1);
        request.setEventDate(LocalDate.now());
        request.setRole("Performer");
        request.setAchievement("Won 1st Prize");
    }

    @Test
    public void addCulturalDetail_Success() throws IOException {
        request.setFile(new MockMultipartFile("file", "certificate.pdf", "application/pdf", "test".getBytes()));

        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        when(culturalEventsRepository.findById(anyInt())).thenReturn(Optional.of(event));
        when(eventCategoryRepository.findById(anyInt())).thenReturn(Optional.of(eventCategory));
        when(culturalDetailRepository.save(any(CulturalDetail.class))).thenReturn(culturalDetail);
        when(tableDetailsRepository.findByTableName("cultural_event_details")).thenReturn(new TableDetails());
        when(requestRepository.save(any(Request.class))).thenReturn(new Request());

        ResponseEntity<?> response = culturalDetailController.addCulturalDetail(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cultural Detail added & Request sent for approval!", response.getBody());
    }

    @Test
    public void getEventNames_Success() {
        when(culturalEventsRepository.findAll()).thenReturn(List.of(event));

        ResponseEntity<List<Map<String, Object>>> response = culturalDetailController.getEventNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void updateStatus_Success() {
        CulturalDetail updatedDetail = new CulturalDetail();
        updatedDetail.setCulturalDetailId(1);
        updatedDetail.setStatus("APPROVED");

        when(culturalDetailService.updateStatus(anyInt(), anyString()))
                .thenReturn(updatedDetail);

        ResponseEntity<String> response = culturalDetailController.updateStatus(1, "APPROVED");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated successfully!", response.getBody());
        verify(culturalDetailService, times(1)).updateStatus(1, "APPROVED");
    }

    @Test
    public void updateStatus_NotFound() {
        when(culturalDetailService.updateStatus(anyInt(), anyString()))
                .thenThrow(new RuntimeException("Cultural Detail not found!"));

        assertThrows(RuntimeException.class, () -> {
            culturalDetailController.updateStatus(999, "APPROVED");
        });
    }
}
