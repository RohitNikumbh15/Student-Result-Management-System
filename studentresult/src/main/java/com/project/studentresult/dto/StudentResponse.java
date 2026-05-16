package com.project.studentresult.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentResponse {
    private Long   id;
    private String rollNumber;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private Integer semester;
    private String username;
    private LocalDateTime createdAt;
}
