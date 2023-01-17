package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    ResetPasswordToken findByUserIdAndCode(Long userId, String code);
}
