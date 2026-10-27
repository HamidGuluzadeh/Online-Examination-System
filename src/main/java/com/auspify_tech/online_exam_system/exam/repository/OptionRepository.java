package com.auspify_tech.online_exam_system.exam.repository;

import com.auspify_tech.online_exam_system.exam.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, String> {

}
