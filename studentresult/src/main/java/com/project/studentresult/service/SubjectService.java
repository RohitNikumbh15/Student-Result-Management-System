package com.project.studentresult.service;

import com.project.studentresult.dto.SubjectRequest;
import com.project.studentresult.model.Subject;
import com.project.studentresult.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject createSubject(SubjectRequest req) {
        if (subjectRepository.existsByCode(req.getCode()))
            throw new RuntimeException("Subject code already exists: " + req.getCode());

        Subject subject = Subject.builder()
                .code(req.getCode())
                .name(req.getName())
                .department(req.getDepartment())
                .semester(req.getSemester())
                .maxMarks(req.getMaxMarks() != null ? req.getMaxMarks() : 100)
                .build();

        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + id));
    }

    public Subject updateSubject(Long id, SubjectRequest req) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + id));
        subject.setName(req.getName());
        subject.setDepartment(req.getDepartment());
        subject.setSemester(req.getSemester());
        subject.setMaxMarks(req.getMaxMarks());
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id))
            throw new RuntimeException("Subject not found: " + id);
        subjectRepository.deleteById(id);
    }
}