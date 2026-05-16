package com.project.studentresult.service;

import com.project.studentresult.dto.ReportResponse;
import com.project.studentresult.dto.ResultRequest;
import com.project.studentresult.dto.ResultResponse;
import com.project.studentresult.model.Result;
import com.project.studentresult.model.Student;
import com.project.studentresult.model.Subject;
import com.project.studentresult.repository.ResultRepository;
import com.project.studentresult.repository.StudentRepository;
import com.project.studentresult.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final ResultRepository  resultRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public ResultService(ResultRepository resultRepository,
                         StudentRepository studentRepository,
                         SubjectRepository subjectRepository) {
        this.resultRepository  = resultRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public ResultResponse addResult(ResultRequest req) {
        Student student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (resultRepository.findByStudentIdAndSubjectIdAndExamType(
                req.getStudentId(), req.getSubjectId(), req.getExamType()).isPresent()) {
            throw new RuntimeException("Result already exists for this student/subject/exam");
        }

        Result result = Result.builder()
                .student(student)
                .subject(subject)
                .examType(req.getExamType())
                .marksObtained(req.getMarksObtained())
                .maxMarks(req.getMaxMarks())
                .remarks(req.getRemarks())
                .examDate(req.getExamDate())
                .build();

        return mapToResponse(resultRepository.save(result));
    }

    public List<ResultResponse> getAllResults() {
        return resultRepository.findAll()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ResultResponse> getResultsByStudent(Long studentId) {
        return resultRepository.findByStudentId(studentId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public ResultResponse updateResult(Long id, ResultRequest req) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found: " + id));
        result.setMarksObtained(req.getMarksObtained());
        result.setMaxMarks(req.getMaxMarks());
        result.setRemarks(req.getRemarks());
        result.setExamDate(req.getExamDate());
        return mapToResponse(resultRepository.save(result));
    }

    @Transactional
    public void deleteResult(Long id) {
        if (!resultRepository.existsById(id))
            throw new RuntimeException("Result not found: " + id);
        resultRepository.deleteById(id);
    }

    public ReportResponse getStudentReport(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<ResultResponse> results = resultRepository
                .findAllByStudentIdOrdered(studentId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());

        int totalObtained = results.stream().mapToInt(ResultResponse::getMarksObtained).sum();
        int totalMax      = results.stream().mapToInt(ResultResponse::getMaxMarks).sum();
        double percentage = totalMax > 0 ? (totalObtained * 100.0 / totalMax) : 0;
        Long passed       = resultRepository.countPassedSubjects(studentId);

        String overallGrade;
        if      (percentage >= 90) overallGrade = "A+";
        else if (percentage >= 80) overallGrade = "A";
        else if (percentage >= 70) overallGrade = "B+";
        else if (percentage >= 60) overallGrade = "B";
        else if (percentage >= 50) overallGrade = "C";
        else                       overallGrade = "F";

        return ReportResponse.builder()
                .studentName(student.getFullName())
                .rollNumber(student.getRollNumber())
                .department(student.getDepartment())
                .semester(student.getSemester())
                .results(results)
                .totalMarksObtained(totalObtained)
                .totalMaxMarks(totalMax)
                .overallPercentage(Math.round(percentage * 100.0) / 100.0)
                .overallGrade(overallGrade)
                .passedSubjects(passed)
                .totalSubjects((long) results.size())
                .status(percentage >= 50 ? "PASS" : "FAIL")
                .build();
    }

    private ResultResponse mapToResponse(Result r) {
        double pct = r.getMaxMarks() > 0
                ? Math.round((r.getMarksObtained() * 100.0 / r.getMaxMarks()) * 100.0) / 100.0
                : 0;
        return ResultResponse.builder()
                .id(r.getId())
                .studentId(r.getStudent().getId())
                .studentName(r.getStudent().getFullName())
                .rollNumber(r.getStudent().getRollNumber())
                .subjectId(r.getSubject().getId())
                .subjectName(r.getSubject().getName())
                .subjectCode(r.getSubject().getCode())
                .examType(r.getExamType())
                .marksObtained(r.getMarksObtained())
                .maxMarks(r.getMaxMarks())
                .percentage(pct)
                .grade(r.getGrade())
                .remarks(r.getRemarks())
                .examDate(r.getExamDate())
                .createdAt(r.getCreatedAt())
                .build();
    }
}