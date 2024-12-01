package com.ojw.planner.app.system.auth.repository;

import com.ojw.planner.app.system.auth.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
