package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.TechnicalEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TechnicalEventsRepository extends JpaRepository<TechnicalEvents, Integer> {

    @Query("SELECT t FROM TechnicalEvents t WHERE LOWER(t.name) = LOWER(:name)")
    TechnicalEvents findByName(@Param("name") String name);

    @Query("SELECT t.name FROM TechnicalEvents t ORDER BY t.name ASC")
    List<String> findAllEventNames();
}
