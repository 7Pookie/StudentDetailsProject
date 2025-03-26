package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
    @Query("SELECT e FROM EventCategory e WHERE LOWER(e.eventCategoryName) = LOWER(:category)")
    EventCategory findByCategory(@Param("category") String category);

    @Query("SELECT e.eventCategoryName FROM EventCategory e ORDER BY e.eventCategoryName ASC")
    List<String> findAllCategoryNames();
}
