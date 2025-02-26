package com.devteria.identityservice.repository;

import com.devteria.identityservice.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

    @Transactional
    @Modifying
    @Query(value = "delete from invalidated_token " +
            "where expiry_time < now()",
            nativeQuery = true)
    void deleteByExpiryTimeAfterNow();
}
