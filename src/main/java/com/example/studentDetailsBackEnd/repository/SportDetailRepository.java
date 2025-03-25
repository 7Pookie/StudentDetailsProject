package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.SportDetail;
import com.example.studentDetailsBackEnd.Model.SportEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SportDetailRepository extends JpaRepository<SportDetail, Integer> {
    Optional<SportDetail> findById(int ID);

    @Query("SELECT t FROM SportDetail t WHERE t.id = :id")
    Optional<SportDetail> findWithEventDetails(@Param("id") int id);
}
