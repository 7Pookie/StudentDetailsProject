package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.ProfessionalSocietyDetailRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProfessionalSocietyDetailControllerTest {

    @Mock private ProfessionalSocietyDetailRepository societyDetailRepository;
    @Mock private ProfessionalSocietyRepository societyRepository;
    @Mock private ProfessionalSocietyFieldRepository fieldRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TableDetailsRepository tableDetailsRepository;
    @Mock private RequestRepository requestRepository;

    @InjectMocks
    private ProfessionalSocietyDetailController controller;

    private ProfessionalSocietyDetailRequest request;
    private Student student;
    private Faculty faculty;
    private ProfessionalSociety society;
    private ProfessionalSocietyField field;
    private ProfessionalSocietyDetail detail;
    private TableDetails tableDetails;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setFacultyID(1);

        student = new Student();
        student.setStudentID(1);
        student.setFaculty(faculty);

        society = new ProfessionalSociety();
        society.setSocietyID(1);
        society.setSocietyName("IEEE");

        field = new ProfessionalSocietyField();
        field.setFieldID(1);
        field.setFieldName("Computer Science");

        tableDetails = new TableDetails();
        tableDetails.setTableID(1);
        tableDetails.setTableName("professional_society_details");

        detail = new ProfessionalSocietyDetail();
        detail.setSocietyDetailsID(1);
        detail.setStudent(student);
        detail.setSociety(society);
        detail.setField(field);
        detail.setStatus("PENDING");

        request = new ProfessionalSocietyDetailRequest();
        request.setStudentID(1);
        request.setSocietyID(1);
        request.setFieldID(1);
        request.setDateJoined(LocalDate.now());
        request.setRole("Member");
        request.setAchievementDetails("Active participation");
        request.setFile(new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes()));
    }

    @Test
    void addProfessionalSocietyDetail_Success() throws IOException {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(societyRepository.findById(1)).thenReturn(Optional.of(society));
        when(fieldRepository.findById(1)).thenReturn(Optional.of(field));
        when(societyDetailRepository.save(any())).thenReturn(detail);
        when(tableDetailsRepository.findByTableName("professional_society_details")).thenReturn(tableDetails);
        when(requestRepository.save(any())).thenReturn(new Request());

        ResponseEntity<?> response = controller.addProfessionalSocietyDetail(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Professional Society Detail added & Request sent for approval!", response.getBody());
    }

    @Test
    void addProfessionalSocietyDetail_StudentNotFound() throws IOException {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.addProfessionalSocietyDetail(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("❌ Student not found!", response.getBody());
    }

    @Test
    void addProfessionalSocietyDetail_NoFaculty() throws IOException {
        student.setFaculty(null);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(societyRepository.findById(1)).thenReturn(Optional.of(society));
        when(fieldRepository.findById(1)).thenReturn(Optional.of(field));
    
        ResponseEntity<?> response = controller.addProfessionalSocietyDetail(request);
    
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No faculty assigned to student!", response.getBody());
    }
    

    @Test
    void addProfessionalSocietyDetail_SocietyNotFound() throws IOException {
        request.setCustomSocietyName(null);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(societyRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.addProfessionalSocietyDetail(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("❌ Society must be selected or entered."));
    }

    @Test
    void addProfessionalSocietyDetail_FieldNotFound() throws IOException {
        request.setCustomFieldName(null);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(societyRepository.findById(1)).thenReturn(Optional.of(society));
        when(fieldRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.addProfessionalSocietyDetail(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("❌ Field must be selected or entered."));
    }

    @Test
    void addProfessionalSocietyDetail_CustomSociety() throws IOException {
        request.setSocietyID(null);
        request.setCustomSocietyName("ACM");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(societyRepository.save(any())).thenReturn(society);
        when(fieldRepository.findById(1)).thenReturn(Optional.of(field));
        when(societyDetailRepository.save(any())).thenReturn(detail);
        when(tableDetailsRepository.findByTableName("professional_society_details")).thenReturn(tableDetails);
        when(requestRepository.save(any())).thenReturn(new Request());

        ResponseEntity<?> response = controller.addProfessionalSocietyDetail(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Professional Society Detail added & Request sent for approval!", response.getBody());
    }

    @Test
    void addProfessionalSocietyDetail_CustomField() throws IOException {
        request.setFieldID(null);
        request.setCustomFieldName("AI");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(societyRepository.findById(1)).thenReturn(Optional.of(society));
        when(fieldRepository.save(any())).thenReturn(field);
        when(societyDetailRepository.save(any())).thenReturn(detail);
        when(tableDetailsRepository.findByTableName("professional_society_details")).thenReturn(tableDetails);
        when(requestRepository.save(any())).thenReturn(new Request());

        ResponseEntity<?> response = controller.addProfessionalSocietyDetail(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Professional Society Detail added & Request sent for approval!", response.getBody());
    }

    @Test
    void getFile_Success() {
        byte[] fileContent = "content".getBytes();
        detail.setOfferLetter(fileContent);
        when(societyDetailRepository.findById(1)).thenReturn(Optional.of(detail));

        ResponseEntity<byte[]> response = controller.getFile(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(fileContent, response.getBody());
    }

    @Test
    void getFile_NotFound() {
        when(societyDetailRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = controller.getFile(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllSocieties_Success() {
        when(societyRepository.findAll()).thenReturn(List.of(society));

        ResponseEntity<List<ProfessionalSociety>> response = controller.getAllSocieties();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void addSociety_Success() {
        ProfessionalSociety newSociety = new ProfessionalSociety();
        newSociety.setSocietyName("ACM");
        when(societyRepository.save(any())).thenReturn(newSociety);

        ResponseEntity<ProfessionalSociety> response = controller.addSociety(newSociety);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllFields_Success() {
        when(fieldRepository.findAll()).thenReturn(List.of(field));

        ResponseEntity<List<ProfessionalSocietyField>> response = controller.getAllFields();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void addField_Success() {
        ProfessionalSocietyField newField = new ProfessionalSocietyField();
        newField.setFieldName("Data Science");
        when(fieldRepository.save(any())).thenReturn(newField);

        ResponseEntity<ProfessionalSocietyField> response = controller.addField(newField);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllProfessionalSocietyDetails_Success() {
        when(societyDetailRepository.findAll()).thenReturn(List.of(detail));

        ResponseEntity<?> response = controller.getAllProfessionalSocietyDetails();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void updateStatus_Success() {
        when(societyDetailRepository.findById(1)).thenReturn(Optional.of(detail));
        when(societyDetailRepository.save(any())).thenReturn(detail);

        ResponseEntity<String> response = controller.updateStatus(1, "APPROVED");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Status updated successfully!", response.getBody());
    }

    @Test
    void updateStatus_NotFound() {
        when(societyDetailRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<String> response = controller.updateStatus(1, "APPROVED");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("❌ Society Detail not found!", response.getBody());
    }
}
