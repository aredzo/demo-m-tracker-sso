package com.aredzo.mtracker.sso.repository;

import com.aredzo.mtracker.sso.entity.SsoTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SsoTokenRepository extends JpaRepository<SsoTokenEntity, UUID> {

    List<SsoTokenEntity> findAllByUserUserIdAndValidByBefore(Integer userId, Instant validBy);

    Optional<SsoTokenEntity> findByTokenAndValidByBefore(UUID token, Instant validBy);


}
