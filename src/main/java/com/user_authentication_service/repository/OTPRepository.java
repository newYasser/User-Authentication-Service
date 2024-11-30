package com.user_authentication_service.repository;

import com.user_authentication_service.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP,Long> {

    Optional<OTP>findByEmail(String email);
}
