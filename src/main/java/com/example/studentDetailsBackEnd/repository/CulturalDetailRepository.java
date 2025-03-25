package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.CulturalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CulturalDetailRepository extends JpaRepository<CulturalDetail, Integer> {
    Optional<CulturalDetail> findById(int ID);

    @Query("SELECT t FROM CulturalDetail t WHERE t.id = :id")
    Optional<CulturalDetail> findWithEventDetails(@Param("id") int id);
}
