package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    boolean existsByEmail(String email);
}
