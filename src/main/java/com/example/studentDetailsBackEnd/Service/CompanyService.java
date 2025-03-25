package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.PlacementCompanyDetail;
import com.example.studentDetailsBackEnd.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;


    public List<PlacementCompanyDetail> getAllCompanies() {
        return companyRepository.findAll();
    }

    public PlacementCompanyDetail addCompany(String companyName) {
        PlacementCompanyDetail company = new PlacementCompanyDetail();
        company.setCompanyName(companyName); 
        return companyRepository.save(company);
    }
}
