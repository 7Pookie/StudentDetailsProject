package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.TechnicalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TechnicalDetailRepository extends JpaRepository<TechnicalDetail, Integer> {
    Optional<TechnicalDetail> findById(int ID);

    @Query("SELECT t FROM TechnicalDetail t WHERE t.id = :id")
    Optional<TechnicalDetail> findWithEventDetails(@Param("id") int id);
}
