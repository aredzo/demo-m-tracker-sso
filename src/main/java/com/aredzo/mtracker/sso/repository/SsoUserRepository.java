package com.aredzo.mtracker.sso.repository;

import com.aredzo.mtracker.sso.entity.SsoUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SsoUserRepository extends JpaRepository<SsoUserEntity, Integer> {

    Optional<SsoUserEntity> findByEmail(String email);

    Optional<SsoUserEntity> findByUserId(Integer userId);

    int deleteByUserId(Integer userId);

}
