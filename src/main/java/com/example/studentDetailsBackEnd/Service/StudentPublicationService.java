package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.Student;
import com.example.studentDetailsBackEnd.Model.StudentPublication;
import com.example.studentDetailsBackEnd.repository.StudentPublicationRepository;
import com.example.studentDetailsBackEnd.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentPublicationService {

    @Autowired
    private StudentPublicationRepository publicationRepository;

    @Autowired
    private StudentRepository studentRepository;  // ✅ Inject Student Repository

    @Transactional  // Ensures database commit
    public StudentPublication addPublication(StudentPublication publication) {
        if (publication.getStudent() == null || publication.getStudent().getStudentID() == 0) {
            throw new IllegalArgumentException("Student ID is missing or invalid.");
        }
        if (publication.getTitle() == null || publication.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Publication title is required.");
        }
        if (publication.getPublicationDate() == null) {
            throw new IllegalArgumentException("Publication date is required.");
        }

        // ✅ Fetch Student Object to associate properly
        Optional<Student> studentOpt = studentRepository.findById(publication.getStudent().getStudentID());
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found with ID: " + publication.getStudent().getStudentID());
        }
        publication.setStudent(studentOpt.get());

        // ✅ Ensure proper date format
        publication.setPublicationDate(LocalDate.parse(publication.getPublicationDate().toString()));

        // ✅ Debugging Logs
        System.out.println("Saving publication: " + publication);

        return publicationRepository.save(publication);
    }

    public List<StudentPublication> getPublications(int studentID) {
        return publicationRepository.findByStudent_StudentID(studentID);
    }
}
