package com.auspify_tech.online_exam_system.auth.repository;

import com.auspify_tech.online_exam_system.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {

}
