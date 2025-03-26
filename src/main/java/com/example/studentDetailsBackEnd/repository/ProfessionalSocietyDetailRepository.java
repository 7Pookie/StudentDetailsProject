package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.ProfessionalSocietyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProfessionalSocietyDetailRepository extends JpaRepository<ProfessionalSocietyDetail, Integer> {
    List<ProfessionalSocietyDetail> findByStudentStudentID(int studentID);

    Optional<ProfessionalSocietyDetail> findById(int ID);

}
