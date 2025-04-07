package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.SportDetailRequest;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
@MockitoSettings(strictness = Strictness.LENIENT)
public class SportDetailControllerTest {

    @Mock private SportDetailRepository sportDetailRepository;
    @Mock private SportEventsRepository sportEventsRepository;
    @Mock private SportCategoryRepository sportCategoryRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TableDetailsRepository tableDetailsRepository;
    @Mock private RequestRepository requestRepository;

    @InjectMocks
    private SportDetailController sportDetailController;

    private SportDetailRequest request;
    private Student student;
    private SportEvents event;
    private SportEventCategory eventCategory;
    private SportDetail sportDetail;

    @BeforeEach
    public void setUp() {
        Faculty faculty = new Faculty();
        faculty.setFacultyId(1);

        student = new Student();
        student.setStudentID(1); // ✅ Corrected field to match controller's expectation
        student.setFaculty(faculty);

        event = new SportEvents("Basketball Tournament");
        event.setEventID(1);

        eventCategory = new SportEventCategory("Competitive");
        eventCategory.setSportEventCategoryID(1);

        sportDetail = new SportDetail();
        sportDetail.setSportDetailID(1);
        sportDetail.setStudent(student);
        sportDetail.setEvent(event);
        sportDetail.setEventCategory(eventCategory);
        sportDetail.setStatus("PENDING");

        request = new SportDetailRequest();
        request.setStudentID(1);
        request.setEventID(1);
        request.setEventCategoryID(1);
        request.setEventDate(LocalDate.now());
        request.setRole("Player");
        request.setAchievement("Champion");
    }

    

    @Test
    public void addSportDetail_NoFile() {
        request.setFile(null);

        ResponseEntity<?> response = sportDetailController.addSportDetail(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("❌ Please upload the certificate!", response.getBody());
    }

    @Test
    public void addSportDetail_StudentNotFound() {
        request.setFile(new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes()));
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = sportDetailController.addSportDetail(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("❌ Student not found!", response.getBody());
    }

    @Test
    public void getEventNames_Success() {
        when(sportEventsRepository.findAll()).thenReturn(List.of(event));

        ResponseEntity<List<Map<String, Object>>> response = sportDetailController.getEventNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Basketball Tournament", response.getBody().get(0).get("sportEventName"));
    }

    @Test
    public void updateStatus_Success() {
        SportDetail updated = new SportDetail();
        updated.setSportDetailID(1);
        updated.setStatus("APPROVED");

        when(sportDetailRepository.findById(1)).thenReturn(Optional.of(sportDetail));
        when(sportDetailRepository.save(any(SportDetail.class))).thenReturn(updated);

        ResponseEntity<String> response = sportDetailController.updateStatus(1, "APPROVED");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Status updated successfully!", response.getBody());
        verify(sportDetailRepository, times(1)).save(any(SportDetail.class));
    }

    @Test
    public void updateStatus_NotFound() {
        when(sportDetailRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<String> response = sportDetailController.updateStatus(99, "REJECTED");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("❌ Sport Detail not found!", response.getBody());
    }

    @Test
    public void getFile_Success() {
        byte[] fileContent = "content".getBytes();
        sportDetail.setOfferLetter(fileContent);
        when(sportDetailRepository.findById(1)).thenReturn(Optional.of(sportDetail));

        ResponseEntity<byte[]> response = sportDetailController.getFile(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(fileContent, response.getBody());
        assertEquals("application/pdf", response.getHeaders().getFirst("Content-Type"));
    }

    @Test
    public void getFile_NotFound() {
        when(sportDetailRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = sportDetailController.getFile(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}