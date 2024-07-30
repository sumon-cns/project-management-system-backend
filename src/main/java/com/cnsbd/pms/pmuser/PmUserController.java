package com.cnsbd.pms.pmuser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class PmUserController {
    private final PmUserService pmUserService;

    @GetMapping(value = "/users")
    public List<PmUserDto> getUsers() {
        return pmUserService.getAllUsers();
    }
}
