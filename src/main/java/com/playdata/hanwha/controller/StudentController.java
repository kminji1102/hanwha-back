package com.playdata.hanwha.controller;

import com.playdata.hanwha.entity.Student;
import com.playdata.hanwha.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:3000") // React 서버 주소
public class StudentController {
    private final StudentRepository studentRepository;

    // 훈련생 전체 조회
    @GetMapping
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    // 훈련생 생성
    @PostMapping("/join")
    public Student createBoard(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // 훈련생 상세 조회
    @GetMapping("/{id}")
    public Student getBoardById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("훈련생이 존재하지 않습니다."));
    }

    // 훈련생 수정
    @PatchMapping("/{id}")
    public Student updateBoard(@PathVariable Long id, @RequestBody Student studentRequest) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("훈련생이 존재하지 않습니다."));

        if (studentRequest.getName() != null) {
            student.setName(studentRequest.getName());
        }
        if (studentRequest.getBirthday() != null) {
            student.setBirthday(studentRequest.getBirthday());
        }

        return studentRepository.save(student);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }
}
