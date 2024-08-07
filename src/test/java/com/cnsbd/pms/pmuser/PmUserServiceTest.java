package com.cnsbd.pms.pmuser;

import static org.junit.jupiter.api.Assertions.*;

import com.cnsbd.pms.pmuser.dto.PmUserDto;
import com.cnsbd.pms.pmuser.service.PmUserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Disabled
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
