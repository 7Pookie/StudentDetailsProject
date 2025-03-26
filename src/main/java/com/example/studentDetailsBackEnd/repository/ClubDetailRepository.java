package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.ClubDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubDetailRepository extends JpaRepository<ClubDetail, Integer> {
}