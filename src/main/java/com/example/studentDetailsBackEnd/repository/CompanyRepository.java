package com.example.studentDetailsBackEnd.repository;

import com.example.studentDetailsBackEnd.Model.PlacementCompanyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<PlacementCompanyDetail, Integer> {
    PlacementCompanyDetail findByCompanyName(String companyName);
}
