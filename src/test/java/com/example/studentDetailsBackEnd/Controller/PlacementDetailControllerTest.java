package com.example.studentDetailsBackEnd.Controller;

import com.example.studentDetailsBackEnd.DTO.PlacementDetailRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlacementDetailControllerTest {

    @Mock
    private PlacementDetailRepository placementDetailRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private TableDetailsRepository tableDetailsRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private PlacementDetailController placementDetailController;

    private PlacementDetailRequest validRequest;
    private Student student;
    private Faculty faculty;
    private PlacementCompanyDetail company;
    private TableDetails tableDetails;
    private PlacementDetail placementDetail;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockFile = new MockMultipartFile("file", "offer.pdf", "application/pdf", "test content".getBytes());
        
        validRequest = new PlacementDetailRequest();
        validRequest.setStudentID(1);
        validRequest.setCompanyID(1);
        validRequest.setPlacementType("Internship");
        validRequest.setStartDate(LocalDate.now());
        validRequest.setEndDate(LocalDate.now().plusMonths(6));
        validRequest.setRole("Software Developer");
        validRequest.setRemark("Test remark");
        validRequest.setFile(mockFile);
        
        faculty = new Faculty();
        faculty.setFacultyID(1);
        faculty.setName("Test Faculty");
        
        student = new Student();
        student.setStudentID(1);
        student.setFaculty(faculty);
        
        company = new PlacementCompanyDetail();
        company.setCompanyID(1);
        company.setCompanyName("Test Company");
        
        tableDetails = new TableDetails();
        tableDetails.setTableID(1);
        tableDetails.setTableName("placement_details");
        
        placementDetail = new PlacementDetail();
        placementDetail.setPlacementID(1);
        placementDetail.setStudent(student);
        placementDetail.setCompany(company);
        placementDetail.setPlacementType("Internship");
        placementDetail.setStartDate(LocalDate.now());
        placementDetail.setEndDate(LocalDate.now().plusMonths(6));
        placementDetail.setRole("Software Developer");
        placementDetail.setStatus("PENDING");
        placementDetail.setRemark("Test remark");
        placementDetail.setOfferLetter("test content".getBytes());
    }

    @Test
    void addPlacementDetail_WithValidDataAndExistingCompany_ShouldReturnSuccess() throws IOException { // Added throws
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(tableDetailsRepository.findByTableName("placement_details")).thenReturn(tableDetails);
        when(placementDetailRepository.save(any(PlacementDetail.class))).thenReturn(placementDetail);
        when(requestRepository.save(any(Request.class))).thenReturn(new Request());

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Placement Detail added & Request sent for approval!", response.getBody());
    }

    @Test
    void addPlacementDetail_WithValidDataAndNewCompany_ShouldReturnSuccess() throws IOException { // Added throws
        validRequest.setCompanyID(null);
        validRequest.setCustomCompanyName("New Company");
        
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(companyRepository.save(any(PlacementCompanyDetail.class))).thenReturn(company);
        when(tableDetailsRepository.findByTableName("placement_details")).thenReturn(tableDetails);
        when(placementDetailRepository.save(any(PlacementDetail.class))).thenReturn(placementDetail);
        when(requestRepository.save(any(Request.class))).thenReturn(new Request());

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Placement Detail added & Request sent for approval!", response.getBody());
    }

    @Test
    void addPlacementDetail_WithInvalidStudentID_ShouldReturnNotFound() throws IOException { // Added throws
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("❌ Student not found!", response.getBody());
    }

    @Test
    void addPlacementDetail_WithStudentWithoutFaculty_ShouldReturnBadRequest() throws IOException { // Added throws
        student.setFaculty(null);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("❌ No faculty assigned to student!", response.getBody());
    }

    @Test
    void addPlacementDetail_WithNonExistentCompany_ShouldReturnNotFound() throws IOException { // Added throws
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(companyRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("❌ Selected company not found.", response.getBody());
    }

    @Test
    void addPlacementDetail_WithNoCompanyInfo_ShouldReturnBadRequest() throws IOException { // Added throws
        validRequest.setCompanyID(null);
        validRequest.setCustomCompanyName(null);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("❌ Company must be selected or entered.", response.getBody());
    }

    @Test
    void addPlacementDetail_WithFileUploadError_ShouldReturnInternalServerError() throws IOException {
        MultipartFile errorFile = mock(MultipartFile.class);
        when(errorFile.getBytes()).thenThrow(new IOException());
        validRequest.setFile(errorFile);
        
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("❌ Error saving file.", response.getBody());
    }

    @Test
    void addPlacementDetail_WithoutTableDetails_ShouldReturnInternalServerError() throws IOException { // Added throws
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(tableDetailsRepository.findByTableName("placement_details")).thenReturn(null);
        when(placementDetailRepository.save(any(PlacementDetail.class))).thenReturn(placementDetail);

        ResponseEntity<?> response = placementDetailController.addPlacementDetail(validRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("❌ Table entry for placement_details not found.", response.getBody());
    }

    @Test
    void updateStatus_WithValidId_ShouldUpdateStatus() {
        when(placementDetailRepository.findById(1)).thenReturn(Optional.of(placementDetail));
        when(placementDetailRepository.save(any(PlacementDetail.class))).thenReturn(placementDetail);

        ResponseEntity<String> response = placementDetailController.updateStatus(1, "APPROVED", "Approved remark");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Status updated successfully!", response.getBody());
    }

    @Test
    void updateStatus_WithInvalidId_ShouldReturnNotFound() {
        when(placementDetailRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<String> response = placementDetailController.updateStatus(1, "APPROVED", "Approved remark");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("❌ Placement Detail not found!", response.getBody());
    }

    @Test
    void getFile_WithValidId_ShouldReturnFile() {
        when(placementDetailRepository.findById(1)).thenReturn(Optional.of(placementDetail));

        ResponseEntity<byte[]> response = placementDetailController.getFile(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals("test content".getBytes(), response.getBody());
    }

    @Test
    void getFile_WithInvalidId_ShouldReturnNotFound() {
        when(placementDetailRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = placementDetailController.getFile(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getFile_WithNoFile_ShouldReturnNotFound() {
        placementDetail.setOfferLetter(null);
        when(placementDetailRepository.findById(1)).thenReturn(Optional.of(placementDetail));

        ResponseEntity<byte[]> response = placementDetailController.getFile(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllPlacements_ShouldReturnList() {
        when(placementDetailRepository.findAll()).thenReturn(Arrays.asList(placementDetail));

        ResponseEntity<?> response = placementDetailController.getAllPlacements();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}