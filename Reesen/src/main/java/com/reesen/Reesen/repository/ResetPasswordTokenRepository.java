package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    ResetPasswordToken findByUserIdAndCode(Long userId, String code);
}
