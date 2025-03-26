package com.example.studentDetailsBackEnd.DTO;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPublicationDTO {
    private int studentID;
    private String title;
    private String type;
    private LocalDate publicationDate;
    private String number;
    private String authors;
    private String publicationStatus;
    private String status;
    private String remark;
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
