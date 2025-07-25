package com.playdata.hanwha.repository;

import com.playdata.hanwha.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
