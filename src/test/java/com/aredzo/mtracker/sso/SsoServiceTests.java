package com.aredzo.mtracker.sso;


import com.aredzo.mtracker.sso.dto.TokenResponse;
import com.aredzo.mtracker.sso.dto.UserTokenResponse;
import com.aredzo.mtracker.sso.dto.ValidateTokenResponse;
import com.aredzo.mtracker.sso.entity.SsoTokenEntity;
import com.aredzo.mtracker.sso.entity.SsoUserEntity;
import com.aredzo.mtracker.sso.entity.UserTypeEnum;
import com.aredzo.mtracker.sso.exception.SsoServiceException;
import com.aredzo.mtracker.sso.repository.SsoTokenRepository;
import com.aredzo.mtracker.sso.repository.SsoUserRepository;
import com.aredzo.mtracker.sso.service.SsoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class SsoServiceTests {

    @Autowired
    private SsoUserRepository userRepository;

    @Autowired
    private SsoTokenRepository tokenRepository;

    private SsoService ssoService;

    private final SsoUserEntity user1 = new SsoUserEntity(
            "email-1",
            "password-1",
            UserTypeEnum.REGULAR_USER,
            Instant.now().plus(1, ChronoUnit.DAYS));

    private final SsoUserEntity user2 = new SsoUserEntity(
            "email-2",
            "password-2",
            UserTypeEnum.SERVICE,
            Instant.now().plus(1, ChronoUnit.DAYS));


    @Before
    public void setUp() {
        ssoService = new SsoService(userRepository, tokenRepository);
        userRepository.deleteAll();
        tokenRepository.deleteAll();
        ;
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }


    @Test
    public void addNewUserTest() {
        UserTokenResponse response = ssoService.addNewUser("email-3", "password-3", UserTypeEnum.REGULAR_USER);

        assertThat(response.getEmail()).isEqualTo("email-3");
        assertThat(response.getUserId()).isNotNull();
        assertThat(response.getToken()).isNotNull();

        int userId = response.getUserId();
        UUID actualToken = response.getToken();

        SsoUserEntity userEntity = userRepository.findByEmail("email-3").orElseThrow(RuntimeException::new);
        assertThat(userEntity.getUserId()).isEqualTo(userId);
        assertThat(userEntity.getTokens()).hasSize(1);
        assertThat(userEntity.getTokens().get(0).getToken()).isEqualTo(actualToken);
        assertThat(userEntity.getTokens().get(0).getValidBy()).isBeforeOrEqualTo(Instant.now().plus(7, ChronoUnit.DAYS));
    }

    @Test
    public void loginExistingUserTest() {
        TokenResponse response = ssoService.login("email-1", "password-1");

        assertThat(response.getValidBy()).isNotNull();
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getValidBy()).isBeforeOrEqualTo(Instant.now().plus(7, ChronoUnit.DAYS));

        UUID actualToken = response.getToken();

        SsoUserEntity userEntity = userRepository.findByEmail("email-1").orElseThrow(RuntimeException::new);
        SsoTokenEntity tokenEntity = tokenRepository.findById(actualToken).orElseThrow(RuntimeException::new);

        assertThat(userEntity.getTokens()).hasSize(2);
        assertThat(tokenEntity.getUser().getEmail()).isEqualTo("email-1");
    }

    @Test
    public void validateValidTokenTest() {
        ValidateTokenResponse response = ssoService.validateToken(user1.getTokens().get(0).getToken());

        assertThat(response.getUserId()).isEqualTo(user1.getUserId());
        assertThat(response.getUserType()).isEqualTo(user1.getUserType());
    }


    @Test(expected = SsoServiceException.class)
    public void validateUnknownTokenTest() {
        ValidateTokenResponse response = ssoService.validateToken(UUID.randomUUID());
    }

    @Test(expected = SsoServiceException.class)
    public void validateExpiredTokenTest() {
        SsoTokenEntity expiredToken = tokenRepository.save(user1.addToken(Instant.now().minus(30, ChronoUnit.MINUTES)));
        ValidateTokenResponse response = ssoService.validateToken(expiredToken.getToken());
    }
}
