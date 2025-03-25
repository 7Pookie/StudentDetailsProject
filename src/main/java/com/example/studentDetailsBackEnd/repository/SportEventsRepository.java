package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.SportEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SportEventsRepository extends JpaRepository<SportEvents, Integer> {

    @Query("SELECT t FROM SportEvents t WHERE LOWER(t.sportEventName) = LOWER(:name)")
    Optional<SportEvents> findBySportEventName(@Param("name") String name);


    @Query("SELECT t.sportEventName FROM SportEvents t ORDER BY t.sportEventName ASC")
    List<String> findAllSportEventNames();
}
