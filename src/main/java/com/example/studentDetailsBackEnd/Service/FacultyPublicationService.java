package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.DTO.FacultyPublicationDTO;
import com.example.studentDetailsBackEnd.Model.FacultyPublication;
import com.example.studentDetailsBackEnd.Repository.FacultyPublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyPublicationService {

    @Autowired
    private FacultyPublicationRepository facultyPublicationRepository;

    public FacultyPublication savePublication(FacultyPublicationDTO dto) {
        FacultyPublication publication = new FacultyPublication();
        publication.setTitle(dto.getTitle());
        publication.setType(dto.getType());
        publication.setPublicationDate(dto.getPublicationDate());
        publication.setNumber(dto.getNumber());
        publication.setAuthors(dto.getAuthors());
        publication.setPublicationStatus(dto.getPublicationStatus());

        return facultyPublicationRepository.save(publication);
    }

    public List<FacultyPublication> getAllPublications() {
        return facultyPublicationRepository.findAll();
    }
}
