package com.cnsbd.pms.pmuser.controller;

import com.cnsbd.pms.pmuser.dto.PmUserDto;
import com.cnsbd.pms.pmuser.service.PmUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@Tag(name = "User Controller", description = "user related api(s)")
public class PmUserController {
    private final PmUserService pmUserService;

    @Operation(summary = "Get all users", description = "get all users form the system")
    @GetMapping(value = "/users")
    public List<PmUserDto> getUsers() {
        return pmUserService.getAllUsers();
    }

    @Operation(
            summary = "Get available users",
            description = "Get available users for an existing project to add")
    @GetMapping(value = "/projects/{projectId}/available-users")
    public List<PmUserDto> getAvailableUsers(@PathVariable int projectId) {
        return pmUserService.getAvailableUsers(projectId);
    }
}
