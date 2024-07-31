package com.cnsbd.pms.pmuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PmUserServiceTest {
    @Autowired private PmUserService pmUserService;

    @Test
    void testGetAvailableUsers() {
        List<PmUserDto> users = pmUserService.getAvailableUsers(7);
        assertNotNull(users);
        System.out.println(users);
    }
}
