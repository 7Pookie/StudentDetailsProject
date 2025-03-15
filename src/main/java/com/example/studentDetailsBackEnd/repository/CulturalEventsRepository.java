package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.CulturalEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CulturalEventsRepository extends JpaRepository<CulturalEvents, Integer> {

    // ✅ Find event by name (Case-Insensitive)
    @Query("SELECT t FROM CulturalEvents t WHERE LOWER(t.name) = LOWER(:name)")
    CulturalEvents findByName(@Param("name") String name);

    // ✅ Fetch all event names only
    @Query("SELECT t.name FROM CulturalEvents t ORDER BY t.name ASC")
    List<String> findAllEventNames();
}
