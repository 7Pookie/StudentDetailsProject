package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.PlacementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacementDetailRepository extends JpaRepository<PlacementDetail, Integer> {
    
}
