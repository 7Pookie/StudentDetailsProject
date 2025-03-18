package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.TechnicalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TechnicalDetailRepository extends JpaRepository<TechnicalDetail, Integer> {
    Optional<TechnicalDetail> findById(int ID);
}
