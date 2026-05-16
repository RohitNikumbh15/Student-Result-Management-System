package com.project.studentresult.repository;


import com.project.studentresult.model.Result;
import com.project.studentresult.model.Result.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentId(Long studentId);
    List<Result> findByStudentIdAndExamType(Long studentId, ExamType examType);

    Optional<Result> findByStudentIdAndSubjectIdAndExamType(
            Long studentId, Long subjectId, ExamType examType);

    List<Result> findBySubjectId(Long subjectId);

    @Query("SELECT r FROM Result r WHERE r.student.id = :studentId ORDER BY r.subject.name")
    List<Result> findAllByStudentIdOrdered(Long studentId);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.student.id = :studentId AND (r.marksObtained * 100.0 / r.maxMarks) >= 50")
    Long countPassedSubjects(Long studentId);
}