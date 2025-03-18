package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.Faculty;

import java.lang.StackWalker.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    boolean existsByEmail(String email);

    Optional<Faculty> findByEmail(String email);
}
