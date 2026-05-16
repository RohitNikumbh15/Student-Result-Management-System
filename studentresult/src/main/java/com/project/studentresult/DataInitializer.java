package com.project.studentresult;

import com.project.studentresult.model.Student;
import com.project.studentresult.model.Subject;
import com.project.studentresult.model.User;
import com.project.studentresult.repository.StudentRepository;
import com.project.studentresult.repository.SubjectRepository;
import com.project.studentresult.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository    userRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder   passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository    = userRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Create ADMIN
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123")) 
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Admin created: admin / admin123");
        }

        // Create STUDENT
        if (!userRepository.existsByUsername("rohit123")) {
            User studentUser = User.builder()
                    .username("rohit123")
                    .password(passwordEncoder.encode("student123"))
                    .role(User.Role.STUDENT)
                    .build();
            studentUser = userRepository.save(studentUser);

            Student student = Student.builder()
                    .user(studentUser)
                    .rollNumber("BCA2024001")
                    .fullName("Rohit Nikumbh")
                    .email("rohit@example.com")
                    .phone("9322563550")
                    .department("BCA")
                    .semester(6)
                    .build();
            studentRepository.save(student);
            System.out.println(">>> Student created: rohit123 / student123");
        }

        // Create Subjects
        String[][] subjects = {
            {"BCA601", "Java Programming",     "BCA", "6", "100"},
            {"BCA602", "Web Development",      "BCA", "6", "100"},
            {"BCA603", "Database Management",  "BCA", "6", "100"},
            {"BCA604", "Software Engineering", "BCA", "6", "100"},
        };

        for (String[] s : subjects) {
            if (!subjectRepository.existsByCode(s[0])) {
                subjectRepository.save(Subject.builder()
                        .code(s[0]).name(s[1]).department(s[2])
                        .semester(Integer.parseInt(s[3]))
                        .maxMarks(Integer.parseInt(s[4]))
                        .build());
                System.out.println(">>> Subject created: " + s[0] + " " + s[1]);
            }
        }

        System.out.println("==============================================");
        System.out.println("  DB ready!  Login: admin/admin123");
        System.out.println("             Login: rohit123/student123");
        System.out.println("==============================================");
    }
}