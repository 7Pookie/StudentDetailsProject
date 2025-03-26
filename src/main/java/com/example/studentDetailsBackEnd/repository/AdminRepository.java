package com.example.studentDetailsBackEnd.repository;


import org.springframework.stereotype.Repository;
import com.example.studentDetailsBackEnd.Model.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
   boolean existsByEmail(String email);
}

