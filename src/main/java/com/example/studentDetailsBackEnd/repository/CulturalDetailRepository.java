package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.CulturalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CulturalDetailRepository extends JpaRepository<CulturalDetail, Integer> {
}
