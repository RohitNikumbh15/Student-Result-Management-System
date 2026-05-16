package com.project.studentresult.controller;


import com.project.studentresult.dto.*;
import com.project.studentresult.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

 
    
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ResultResponse>>> getAllResults() {
        return ResponseEntity.ok(ApiResponse.success("All results fetched", resultService.getAllResults()));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ResultResponse>> addResult(@Valid @RequestBody ResultRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Result added", resultService.addResult(req)));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ResultResponse>> updateResult(
            @PathVariable Long id, @Valid @RequestBody ResultRequest req) {
        return ResponseEntity.ok(ApiResponse.success("Result updated", resultService.updateResult(id, req)));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.ok(ApiResponse.success("Result deleted", null));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<List<ResultResponse>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Results fetched", resultService.getResultsByStudent(studentId)));
    }

    @GetMapping("/report/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<ApiResponse<ReportResponse>> getReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Report generated", resultService.getStudentReport(studentId)));
    }
}