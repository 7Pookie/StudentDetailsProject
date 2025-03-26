package com.example.studentDetailsBackEnd.DTO;

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

}
