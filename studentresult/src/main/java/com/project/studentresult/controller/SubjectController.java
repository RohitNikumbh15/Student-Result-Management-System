package com.project.studentresult.controller;


import com.project.studentresult.dto.*;
import com.project.studentresult.model.Subject;
import com.project.studentresult.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Subject>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Subjects fetched", subjectService.getAllSubjects()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Subject>> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Subject fetched", subjectService.getSubjectById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Subject>> create(@Valid @RequestBody SubjectRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subject created", subjectService.createSubject(req)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Subject>> update(@PathVariable Long id, @Valid @RequestBody SubjectRequest req) {
        return ResponseEntity.ok(ApiResponse.success("Subject updated", subjectService.updateSubject(id, req)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.success("Subject deleted", null));
    }
}