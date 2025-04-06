package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.TechnicalDetailRequest;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import com.example.studentDetailsBackEnd.Service.TechnicalDetailService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TechnicalDetailControllerTest {

    @Mock private TechnicalDetailService technicalDetailService;
    @Mock private TechnicalDetailRepository technicalDetailRepository;
    @Mock private TechnicalEventsRepository technicalEventsRepository;
    @Mock private EventCategoryRepository eventCategoryRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TableDetailsRepository tableDetailsRepository;
    @Mock private RequestRepository requestRepository;

    @InjectMocks
    private TechnicalDetailController technicalDetailController;

    private TechnicalDetailRequest request;
    private Student student;
    private TechnicalEvents event;
    private EventCategory eventCategory;
    private TechnicalDetail technicalDetail;

    @BeforeEach
    public void setUp() {
        Faculty faculty = new Faculty();
        faculty.setFacultyId(1);

        student = new Student();
        student.setStudentId(1);
        student.setFaculty(faculty);

        event = new TechnicalEvents();
        event.setEventId(1);
        event.setName("Tech Conference");

        eventCategory = new EventCategory();
        eventCategory.setEventCategoryId(1);
        eventCategory.setEventCategoryName("Workshop");

        technicalDetail = new TechnicalDetail();
        technicalDetail.setTechnicalDetailId(1);
        technicalDetail.setStudent(student);
        technicalDetail.setEvent(event);
        technicalDetail.setEventCategory(eventCategory);
        technicalDetail.setStatus("PENDING");

        request = new TechnicalDetailRequest();
        request.setStudentID(1);
        request.setEventID(1);
        request.setEventCategoryID(1);
        request.setEventDate(LocalDate.now());
        request.setRole("Participant");
        request.setAchievement("Winner");
    }

    @Test
    public void addTechnicalDetail_Success() throws IOException {
        request.setFile(new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes()));

        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        when(technicalEventsRepository.findById(anyInt())).thenReturn(Optional.of(event));
        when(eventCategoryRepository.findById(anyInt())).thenReturn(Optional.of(eventCategory));
        when(technicalDetailRepository.save(any(TechnicalDetail.class))).thenReturn(technicalDetail);
        when(tableDetailsRepository.findByTableName("technical_event_details")).thenReturn(new TableDetails());
        when(requestRepository.save(any(Request.class))).thenReturn(new Request());

        ResponseEntity<?> response = technicalDetailController.addTechnicalDetail(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Technical Detail added & Request sent for approval!", response.getBody());
    }

    @Test
    public void getEventNames_Success() {
        when(technicalEventsRepository.findAll()).thenReturn(List.of(event));

        ResponseEntity<List<Map<String, Object>>> response = technicalDetailController.getEventNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void updateStatus_Success() {
        TechnicalDetail updated = new TechnicalDetail();
        updated.setTechnicalDetailId(1);
        updated.setStatus("APPROVED");

        when(technicalDetailService.updateStatus(anyInt(), anyString())).thenReturn(updated);

        ResponseEntity<String> response = technicalDetailController.updateStatus(1, "APPROVED");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated successfully!", response.getBody());
        verify(technicalDetailService, times(1)).updateStatus(1, "APPROVED");
    }

    @Test
    public void updateStatus_NotFound() {
        when(technicalDetailService.updateStatus(anyInt(), anyString()))
                .thenThrow(new RuntimeException("Technical Detail not found!"));

        assertThrows(RuntimeException.class, () -> {
            technicalDetailController.updateStatus(99, "REJECTED");
        });
    }
}
