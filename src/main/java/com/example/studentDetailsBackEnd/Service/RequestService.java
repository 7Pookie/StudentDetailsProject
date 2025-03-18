package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.Request;
import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.Faculty;
import com.example.studentDetailsBackEnd.Model.TableDetails;
import com.example.studentDetailsBackEnd.repository.RequestRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.FacultyRepository;
import com.example.studentDetailsBackEnd.repository.TableDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TableDetailsRepository tableDetailsRepository;

    // ✅ Method to create a new request
    public void createRequest(int studentID, int tableID, int entryID) {
        Optional<Student> studentOpt = studentRepository.findById(studentID);

        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }

        Student student = studentOpt.get();
        Faculty faculty = student.getFaculty(); // Get the faculty associated with the student

        if (faculty == null) {
            throw new RuntimeException("No faculty advisor assigned to this student");
        }

        TableDetails tableDetails = tableDetailsRepository.findById(tableID)
                .orElseThrow(() -> new RuntimeException("Invalid table ID"));

        Request request = new Request();
        request.setStudent(student);
        request.setFaculty(faculty);
        request.setTableDetails(tableDetails);
        request.setEntryID(entryID);
        request.setStatus("PENDING");

        requestRepository.save(request);
    }

    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    public List<Request> getRequestsForFaculty(int facultyID) {
        return requestRepository.findByFacultyFacultyIDAndStatus(facultyID, "PENDING");
    }
}
