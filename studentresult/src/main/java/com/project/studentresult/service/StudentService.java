package com.project.studentresult.service;

import com.project.studentresult.dto.StudentRequest;
import com.project.studentresult.dto.StudentResponse;
import com.project.studentresult.model.Student;
import com.project.studentresult.model.User;
import com.project.studentresult.repository.StudentRepository;
import com.project.studentresult.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository    userRepository;
    private final PasswordEncoder   passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository    = userRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest req) {
        if (userRepository.existsByUsername(req.getUsername()))
            throw new RuntimeException("Username already exists: " + req.getUsername());
        if (studentRepository.existsByRollNumber(req.getRollNumber()))
            throw new RuntimeException("Roll number already exists: " + req.getRollNumber());
        if (studentRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already exists: " + req.getEmail());

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.STUDENT)
                .build();
        user = userRepository.save(user);

        Student student = Student.builder()
                .user(user)
                .rollNumber(req.getRollNumber())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .department(req.getDepartment())
                .semester(req.getSemester())
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public StudentResponse getStudentById(Long id) {
        return mapToResponse(studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id)));
    }

    public StudentResponse getStudentByUserId(Long userId) {
        return mapToResponse(studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found for userId: " + userId)));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest req) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));
        student.setFullName(req.getFullName());
        student.setEmail(req.getEmail());
        student.setPhone(req.getPhone());
        student.setDepartment(req.getDepartment());
        student.setSemester(req.getSemester());
        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));
        userRepository.deleteById(student.getUser().getId());
    }

    public List<StudentResponse> searchStudents(String name) {
        return studentRepository.searchByName(name)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private StudentResponse mapToResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .rollNumber(s.getRollNumber())
                .fullName(s.getFullName())
                .email(s.getEmail())
                .phone(s.getPhone())
                .department(s.getDepartment())
                .semester(s.getSemester())
                .username(s.getUser().getUsername())
                .createdAt(s.getCreatedAt())
                .build();
    }
}