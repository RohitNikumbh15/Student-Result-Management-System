package com.project.studentresult.dto;

import com.project.studentresult.model.Result.ExamType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResultResponse {
    private Long      id;
    private Long      studentId;
    private String    studentName;
    private String    rollNumber;
    private Long      subjectId;
    private String    subjectName;
    private String    subjectCode;
    private ExamType  examType;
    private Integer   marksObtained;
    private Integer   maxMarks;
    private Double    percentage;
    private String    grade;
    private String    remarks;
    private LocalDate examDate;
    private LocalDateTime createdAt;
}
 