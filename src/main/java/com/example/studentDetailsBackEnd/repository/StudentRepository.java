package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsByEmail(String email);

    Optional<Student> findByEmail(String email);
}
