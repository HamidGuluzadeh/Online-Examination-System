package com.auspify_tech.online_exam_system.exam.repository;

import com.auspify_tech.online_exam_system.exam.entity.ExamEntity;
import com.auspify_tech.online_exam_system.exam.model.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, String> {

    Page<ExamEntity> findAllByStatus(ExamStatus status, Pageable pageable);

    Page<ExamEntity> findAllByCreatedBy(String id, Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);

}
