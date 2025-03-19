package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByFacultyFacultyIDAndStatus(int facultyID, String status);

    Optional<Request> findById(int requestID);

    List<Request> findByStudentStudentID(int studentID);
}
