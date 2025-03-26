package com.example.studentDetailsBackEnd.Service;

import com.example.studentDetailsBackEnd.Model.ClubDetail;
import com.example.studentDetailsBackEnd.repository.ClubDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClubDetailService {

    @Autowired
    private ClubDetailRepository clubDetailRepository;

    public List<ClubDetail> getAllClubDetails() {
        return clubDetailRepository.findAll();
    }

    public Optional<ClubDetail> getClubDetailById(int id) {
        return clubDetailRepository.findById(id);
    }

    public ClubDetail addClubDetail(ClubDetail clubDetail) {
        return clubDetailRepository.save(clubDetail);
    }

    public void updateClubDetail(int id, ClubDetail clubDetail) {
        if (clubDetailRepository.existsById(id)) {
            clubDetail.setClubDetailID(id);
            clubDetailRepository.save(clubDetail);
        }
    }

    public void deleteClubDetail(int id) {
        clubDetailRepository.deleteById(id);
    }
}