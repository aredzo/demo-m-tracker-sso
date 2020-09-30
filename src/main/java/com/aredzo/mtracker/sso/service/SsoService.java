package com.aredzo.mtracker.sso.service;

import com.aredzo.mtracker.sso.dto.TokenResponse;
import com.aredzo.mtracker.sso.dto.UserResponse;
import com.aredzo.mtracker.sso.dto.UserTokenResponse;
import com.aredzo.mtracker.sso.dto.ValidateTokenResponse;
import com.aredzo.mtracker.sso.dto.mapper.SsoUserMapper;
import com.aredzo.mtracker.sso.entity.SsoTokenEntity;
import com.aredzo.mtracker.sso.entity.SsoUserEntity;
import com.aredzo.mtracker.sso.entity.UserTypeEnum;
import com.aredzo.mtracker.sso.exception.SsoServiceError;
import com.aredzo.mtracker.sso.exception.SsoServiceException;
import com.aredzo.mtracker.sso.repository.SsoTokenRepository;
import com.aredzo.mtracker.sso.repository.SsoUserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SsoService {


    private final SsoUserRepository ssoUserRepository;
    private final SsoTokenRepository ssoTokenRepository;
    private final SsoUserMapper ssoUserMapper;


    public SsoService(SsoUserRepository ssoUserRepository, SsoTokenRepository ssoTokenRepository) {
        this.ssoUserRepository = ssoUserRepository;
        this.ssoTokenRepository = ssoTokenRepository;
        this.ssoUserMapper = Mappers.getMapper(SsoUserMapper.class);
    }

    public UserTokenResponse addNewUser(String email, String password, UserTypeEnum userType) {
        SsoUserEntity user = ssoUserRepository.save(new SsoUserEntity(email, password, userType, Instant.now().plus(7, ChronoUnit.DAYS)));

        return new UserTokenResponse(
                user.getUserId(),
                user.getEmail(),
                user.getTokens().stream().findFirst().orElseThrow(() -> new SsoServiceException(SsoServiceError.INTERNAL_ERROR)).getToken());
    }

    public List<UserResponse> getAllUsers() {
        return ssoUserRepository.findAll().stream().map(ssoUserMapper::ssoUserEntityToResponse).collect(Collectors.toList());
    }

    public UserResponse getUserWithId(int userId) {
        return ssoUserMapper.ssoUserEntityToResponse(getUserById(userId));
    }

    public TokenResponse login(String email, String password) {
        SsoUserEntity user = getUserByEmail(email);
        if (!user.getPassword().equals(password)) {
            throw new SsoServiceException(SsoServiceError.NOT_AUTHORIZED);
        }
        SsoTokenEntity token = user.addToken(Instant.now().plus(7, ChronoUnit.DAYS));
        ssoUserRepository.save(user);
        return new TokenResponse(token.getToken(), token.getValidBy());
    }

    public ValidateTokenResponse validateToken(UUID token) {
        SsoUserEntity user = ssoTokenRepository.findByTokenAndValidByBefore(token, Instant.now()).
                orElseThrow(() -> new SsoServiceException(SsoServiceError.NOT_AUTHORIZED))
                .getUser();
        return new ValidateTokenResponse(user.getUserId(), user.getUserType());
    }

    private SsoUserEntity getUserByEmail(String email) {
        return ssoUserRepository.findByEmail(email).orElseThrow(() -> new SsoServiceException(SsoServiceError.USER_NOT_FOUND));
    }

    private SsoUserEntity getUserById(int userId) {
        return ssoUserRepository.findByUserId(userId).orElseThrow(() -> new SsoServiceException(SsoServiceError.USER_NOT_FOUND));
    }


}
