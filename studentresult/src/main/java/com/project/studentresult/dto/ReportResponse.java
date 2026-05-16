package com.project.studentresult.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportResponse {
    private String studentName;
    private String rollNumber;
    private String department;
    private Integer semester;
    private List<ResultResponse> results;
    private Integer totalMarksObtained;
    private Integer totalMaxMarks;
    private Double  overallPercentage;
    private String  overallGrade;
    private Long    passedSubjects;
    private Long    totalSubjects;
    private String  status;
}