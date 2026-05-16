package com.project.studentresult.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
 
/**
 * Subject — JPA Entity
 * ----------------------
 * Maps to the `subjects` table.
 * Represents a course/paper that students are examined in.
 */
@Entity
@Table(name = "subjects")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true, length = 20)
    private String code;           // e.g. "BCA601"
 
    @Column(nullable = false, length = 100)
    private String name;           // e.g. "Java Programming"
 
    @Column(length = 50)
    private String department;     // e.g. "BCA"
 
    private Integer semester;      // 1–8
 
    @Column(name = "max_marks")
    private Integer maxMarks = 100;
 
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
 
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public Integer getMaxMarks() {
		return maxMarks;
	}

	public void setMaxMarks(Integer maxMarks) {
		this.maxMarks = maxMarks;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
}
