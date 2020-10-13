package com.aredzo.mtracker.sso.service;

import com.aredzo.mtracker.sso.dto.TokenResponse;
import com.aredzo.mtracker.sso.dto.UserResponse;
import com.aredzo.mtracker.sso.dto.UserTokenResponse;
import com.aredzo.mtracker.sso.dto.mapper.SsoUserMapper;
import com.aredzo.mtracker.sso.entity.SsoTokenEntity;
import com.aredzo.mtracker.sso.entity.SsoUserEntity;
import com.aredzo.mtracker.sso.entity.UserTypeEnum;
import com.aredzo.mtracker.sso.exception.SsoServiceError;
import com.aredzo.mtracker.sso.exception.SsoServiceException;
import com.aredzo.mtracker.sso.repository.SsoTokenRepository;
import com.aredzo.mtracker.sso.repository.SsoUserRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(SsoService.class);


    public SsoService(SsoUserRepository ssoUserRepository, SsoTokenRepository ssoTokenRepository) {
        this.ssoUserRepository = ssoUserRepository;
        this.ssoTokenRepository = ssoTokenRepository;
        this.ssoUserMapper = Mappers.getMapper(SsoUserMapper.class);
    }

    public UserTokenResponse addNewUser(String email, String password, UserTypeEnum userType) {
        log.info(String.format("Adding new user with email: %s, password: %s, usertype: %s", email, password, userType.getText()));

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
        log.info(String.format("User logins with email: %s, password: %s", email, password));

        SsoUserEntity user = getUserByEmail(email);
        if (!user.getPassword().equals(password)) {
            throw new SsoServiceException(SsoServiceError.USER_NOT_AUTHORIZED);
        }
        SsoTokenEntity token = ssoTokenRepository.save(user.addToken(Instant.now().plus(7, ChronoUnit.DAYS)));
        return new TokenResponse(token.getToken(), token.getValidBy());
    }

    public void validateServiceToken(UUID token) {
        log.info(String.format("Validating service token: %s", token.toString()));
        SsoTokenEntity tokenEntity = ssoTokenRepository.findById(token)
                .orElseThrow(() -> new SsoServiceException(SsoServiceError.SERVICE_TOKEN_NOT_FOUND));
        if (tokenEntity.getValidBy().isBefore(Instant.now()) && !tokenEntity.getUser().getUserType().equals(UserTypeEnum.SERVICE)) {
            throw new SsoServiceException(SsoServiceError.SERVICE_NOT_AUTHORIZED);
        }
    }

    public SsoTokenEntity validateTokenAndGetTokenEntity(UUID token) {
        log.info(String.format("Validating token: %s", token.toString()));
        SsoTokenEntity tokenEntity = ssoTokenRepository.findById(token)
                .orElseThrow(() -> new SsoServiceException(SsoServiceError.TOKEN_NOT_FOUND));
        if (tokenEntity.getValidBy().isBefore(Instant.now())) {
            throw new SsoServiceException(SsoServiceError.USER_NOT_AUTHORIZED);
        } else {
            return tokenEntity;
        }
    }

    private SsoUserEntity getUserByEmail(String email) {
        return ssoUserRepository.findByEmail(email).orElseThrow(() -> new SsoServiceException(SsoServiceError.USER_NOT_FOUND));
    }

    private SsoUserEntity getUserById(int userId) {
        return ssoUserRepository.findByUserId(userId).orElseThrow(() -> new SsoServiceException(SsoServiceError.USER_NOT_FOUND));
    }


}
