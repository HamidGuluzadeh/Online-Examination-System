package com.auspify_tech.online_exam_system.exam.repository;

import com.auspify_tech.online_exam_system.exam.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, String> {

}
