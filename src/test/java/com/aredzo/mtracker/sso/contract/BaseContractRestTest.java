package com.aredzo.mtracker.sso.contract;

import com.aredzo.mtracker.sso.MTrackerSsoApplication;
import com.aredzo.mtracker.sso.controller.ExceptionController;
import com.aredzo.mtracker.sso.controller.SsoPrivateController;
import com.aredzo.mtracker.sso.controller.SsoPublicController;
import com.aredzo.mtracker.sso.dto.UserTokenResponse;
import com.aredzo.mtracker.sso.entity.UserTypeEnum;
import com.aredzo.mtracker.sso.service.SsoService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MTrackerSsoApplication.class)
public abstract class BaseContractRestTest {

    @Autowired
    public SsoPublicController ssoPublicController;

    @Autowired
    public SsoPrivateController ssoPrivateController;

    @Autowired
    public ExceptionController exceptionController;

    @MockBean
    public SsoService ssoService;

    @Before
    public void setUp() throws Exception {
        RestAssuredMockMvc.standaloneSetup(ssoPrivateController, ssoPublicController, exceptionController);

        Mockito.when(ssoService.addNewUser(anyString(), anyString(), any(UserTypeEnum.class)))
                .thenAnswer((Answer<UserTokenResponse>) invocationOnMock -> {
                    String email = invocationOnMock.getArgument(0);
                    return new UserTokenResponse(1, email, UUID.randomUUID());
                });
    }
}
