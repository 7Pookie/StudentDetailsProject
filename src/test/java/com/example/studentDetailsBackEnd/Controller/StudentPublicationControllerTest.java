package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.StudentPublicationDTO;
import com.example.studentDetailsBackEnd.Model.*;
import com.example.studentDetailsBackEnd.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class StudentPublicationControllerTest {

    @Mock
    private StudentPublicationRepository studentPublicationRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TableDetailsRepository tableDetailsRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private StudentPublicationController studentPublicationController;

    private StudentPublicationDTO validPublicationDTO;
    private StudentPublicationDTO invalidPublicationDTO;
    private Student student;
    private Faculty faculty;
    private TableDetails tableDetails;
    private StudentPublication publication;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", "test content".getBytes());
        
        validPublicationDTO = new StudentPublicationDTO();
        validPublicationDTO.setStudentID(1);
        validPublicationDTO.setTitle("Test Publication");
        validPublicationDTO.setType("Journal");
        validPublicationDTO.setPublicationDate(LocalDate.now());
        validPublicationDTO.setNumber("1234-5678");
        validPublicationDTO.setAuthors("Author1, Author2");
        validPublicationDTO.setPublicationStatus("Published");
        validPublicationDTO.setFile(mockFile);
        
        invalidPublicationDTO = new StudentPublicationDTO();
        invalidPublicationDTO.setStudentID(0);
        
        faculty = new Faculty();
        faculty.setFacultyID(1);
        faculty.setName("Test Faculty");
        
        student = new Student();
        student.setStudentID(1);
        student.setFaculty(faculty);
        
        tableDetails = new TableDetails();
        tableDetails.setTableID(1);
        tableDetails.setTableName("student_publications");
        
        publication = new StudentPublication();
        publication.setStudentPubID(1);
        publication.setStudent(student);
        publication.setTitle("Test Publication");
        publication.setType("Journal");
        publication.setPublicationDate(LocalDate.now());
        publication.setNumber("1234-5678");
        publication.setAuthors("Author1, Author2");
        publication.setPublicationStatus("Published");
        publication.setStatus("PENDING");
        publication.setOfferLetter("test content".getBytes());
    }

    @Test
    void addPublication_WithValidData_ShouldReturnSuccess() throws IOException {
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        when(tableDetailsRepository.findByTableName("student_publications")).thenReturn(tableDetails);
        when(studentPublicationRepository.save(any(StudentPublication.class))).thenReturn(publication);
        when(requestRepository.save(any(Request.class))).thenReturn(new Request());

        ResponseEntity<?> response = studentPublicationController.addPublication(validPublicationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Publication added & Request sent for approval!", response.getBody());
        verify(studentPublicationRepository, times(1)).save(any(StudentPublication.class));
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void addPublication_WithInvalidStudentID_ShouldReturnBadRequest() {
        ResponseEntity<?> response = studentPublicationController.addPublication(invalidPublicationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid student ID received!", response.getBody());
        verify(studentPublicationRepository, never()).save(any(StudentPublication.class));
    }

    @Test
    void addPublication_WithNonExistentStudent_ShouldReturnNotFound() {
        when(studentRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> response = studentPublicationController.addPublication(validPublicationDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Student not found!", response.getBody());
        verify(studentPublicationRepository, never()).save(any(StudentPublication.class));
    }

    @Test
    void addPublication_WithStudentWithoutFaculty_ShouldReturnBadRequest() {
        student.setFaculty(null);
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));

        ResponseEntity<?> response = studentPublicationController.addPublication(validPublicationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No faculty assigned to student!", response.getBody());
        verify(studentPublicationRepository, never()).save(any(StudentPublication.class));
    }

    @Test
    void addPublication_WithFileUploadError_ShouldReturnInternalServerError() throws IOException {
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        when(tableDetailsRepository.findByTableName("student_publications")).thenReturn(tableDetails);
        
        MultipartFile errorFile = mock(MultipartFile.class);
        when(errorFile.getBytes()).thenThrow(new IOException());
        validPublicationDTO.setFile(errorFile);

        ResponseEntity<?> response = studentPublicationController.addPublication(validPublicationDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error saving file", response.getBody());
        verify(studentPublicationRepository, never()).save(any(StudentPublication.class));
    }

    @Test
    void addPublication_WithoutTableDetails_ShouldReturnInternalServerError() {
        when(studentRepository.findById(anyInt())).thenReturn(Optional.of(student));
        when(tableDetailsRepository.findByTableName("student_publications")).thenReturn(null);
        when(studentPublicationRepository.save(any(StudentPublication.class))).thenReturn(publication);

        ResponseEntity<?> response = studentPublicationController.addPublication(validPublicationDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Table entry for student_publications not found.", response.getBody());
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void getFile_WithValidId_ShouldReturnFile() {
        when(studentPublicationRepository.findById(1)).thenReturn(Optional.of(publication));

        ResponseEntity<byte[]> response = studentPublicationController.getFile(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals("test content".getBytes(), response.getBody());
        assertEquals("application/pdf", response.getHeaders().getFirst("Content-Type"));
        assertEquals("attachment; filename=\"offer_letter.pdf\"", 
            response.getHeaders().getFirst("Content-Disposition"));
    }

    @Test
    void getFile_WithInvalidId_ShouldReturnNotFound() {
        when(studentPublicationRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = studentPublicationController.getFile(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getFile_WithNoFile_ShouldReturnNotFound() {
        publication.setOfferLetter(null);
        when(studentPublicationRepository.findById(1)).thenReturn(Optional.of(publication));

        ResponseEntity<byte[]> response = studentPublicationController.getFile(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getPublicationsByStudent_ShouldReturnList() {
        List<StudentPublication> publications = Arrays.asList(publication);
        when(studentPublicationRepository.findByStudent_StudentID(1)).thenReturn(publications);

        ResponseEntity<List<StudentPublication>> response = studentPublicationController.getPublicationsByStudent(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Publication", response.getBody().get(0).getTitle());
    }

    @Test
    void updateStatus_WithValidId_ShouldUpdateStatus() {
        when(studentPublicationRepository.findById(1)).thenReturn(Optional.of(publication));
        when(studentPublicationRepository.save(any(StudentPublication.class))).thenReturn(publication);

        ResponseEntity<String> response = studentPublicationController.updateStatus(1, "APPROVED");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Publication status updated successfully!", response.getBody());
        assertEquals("APPROVED", publication.getStatus());
        verify(studentPublicationRepository, times(1)).save(publication);
    }

    @Test
    void updateStatus_WithInvalidId_ShouldReturnNotFound() {
        when(studentPublicationRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<String> response = studentPublicationController.updateStatus(1, "APPROVED");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Publication not found!", response.getBody());
        verify(studentPublicationRepository, never()).save(any(StudentPublication.class));
    }
}