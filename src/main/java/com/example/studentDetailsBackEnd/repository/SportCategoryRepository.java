package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.SportEventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SportCategoryRepository extends JpaRepository<SportEventCategory, Integer> {
    @Query("SELECT e FROM SportEventCategory e WHERE LOWER(e.sportCategoryName) = LOWER(:category)")
    SportEventCategory findByCategory(@Param("category") String category);

    @Query("SELECT e.sportCategoryName FROM SportEventCategory e ORDER BY e.sportCategoryName ASC")
    List<String> findAllCategoryNames();
}
