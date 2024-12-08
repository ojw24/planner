package com.ojw.planner.app.system.auth.repository;

import com.ojw.planner.app.system.auth.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByRefreshToken(String refreshToken);

}
