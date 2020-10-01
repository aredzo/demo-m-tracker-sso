package com.aredzo.mtracker.sso.repository;


import com.aredzo.mtracker.sso.entity.SsoTokenEntity;
import com.aredzo.mtracker.sso.entity.SsoUserEntity;
import com.aredzo.mtracker.sso.entity.UserTypeEnum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class SsoRepositoryTests {

    @Autowired
    private SsoUserRepository userRepository;

    @Autowired
    private SsoTokenRepository tokenRepository;

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
        userRepository.deleteAll();
        tokenRepository.deleteAll();;
        userRepository.save(user1);
        userRepository.save(user2);

    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    public void findByEmailReturnsCorrectResultTest() {
        Optional<SsoUserEntity> result = userRepository.findByEmail(user1.getEmail());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user1);
        assertThat(result.get().getTokens()).hasSize(1);
        assertThat(result.get().getTokens().get(0).getValidBy()).isAfter(Instant.now());
        assertThat(result.get().getTokens().get(0).getValidBy()).isBeforeOrEqualTo(Instant.now().plus(1, ChronoUnit.DAYS));
    }

    @Test
    public void deleteUserByIdRemovesUserAndRelatedTokensTest() {
        SsoUserEntity result = userRepository.findByEmail(user1.getEmail()).orElseThrow(RuntimeException::new);
        int userId = result.getUserId();

        assertThat(userRepository.deleteByUserId(userId)).isEqualTo(1);
        List<SsoUserEntity> resultAfterDelete = userRepository.findAll();
        assertThat(resultAfterDelete).hasSize(1);
        assertThat(resultAfterDelete).contains(user2);

        assertThat(tokenRepository.findAll()).hasSize(1);
        assertThat(tokenRepository.findAll().get(0).getUser()).isEqualTo(user2);
    }

    @Test
    public void tokenRepositoryFindByTokenAndValidDateReturnsOnlyIfValidTest(){
        SsoTokenEntity user2ValidToken = user2.getTokens().get(0);
        SsoTokenEntity user2InvalidToken = user2.addToken(Instant.now().minus(30, ChronoUnit.MINUTES));

        tokenRepository.save(user2InvalidToken);

        Optional<SsoTokenEntity> result1 = tokenRepository.findByTokenAndValidByAfter(user2ValidToken.getToken(), Instant.now());
        assertThat(result1.isPresent()).isTrue();
        assertThat(result1.get().getUser()).isEqualTo(user2);

        Optional<SsoTokenEntity> result2 = tokenRepository.findByTokenAndValidByAfter(user2InvalidToken.getToken(), Instant.now());
        assertThat(result2.isPresent()).isFalse();
    }
}
