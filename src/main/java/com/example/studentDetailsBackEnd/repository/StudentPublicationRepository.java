package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.StudentPublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentPublicationRepository extends JpaRepository<StudentPublication, Integer> {
    List<StudentPublication> findByStudent_StudentID(int studentID);

}
