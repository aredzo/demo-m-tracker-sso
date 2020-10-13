package com.aredzo.mtracker.sso;

import com.aredzo.mtracker.sso.controller.SsoPrivateController;
import com.aredzo.mtracker.sso.controller.SsoPublicController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MTrackerSsoApplicationTest {

    @Autowired
    private SsoPrivateController privateController;

    @Autowired
    private SsoPublicController publicController;

    @Test
    public void contextLoadsTest() {
        assertNotNull(privateController);
        assertNotNull(publicController);
    }
}
