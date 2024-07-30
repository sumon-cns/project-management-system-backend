package com.cnsbd.pms.project;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping(value = "/users/{userId}/projects/all")
    public List<ProjectDto> getAllProjects(@PathVariable Integer userId) {
        return projectService.getAllProjects(userId);
    }

    @PostMapping(value = "/projects")
    public String createProject(@Valid @RequestBody ProjectDto dto) {
        projectService.createProject(dto);
        return "Project created successfully";
    }

    @PutMapping(value = "/projects/{projectId}/users")
    public String addUsers(@PathVariable Integer projectId, @RequestBody List<Integer> users) {
        projectService.addUsers(projectId, users);
        return "User(s) added successfully";
    }

    @GetMapping(value = "/users/{userId}/projects")
    public List<ProjectDto> getAllProjects(
            @PathVariable Integer userId,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate) {
        return projectService.getAllProjects(userId, fromDate, toDate);
    }

    @DeleteMapping(value = "/projects/{projectId}")
    public String deleteProject(@PathVariable Integer projectId) {
        projectService.deleteProject(projectId);
        return "Project deleted successfully";
    }

    @PutMapping(value = "/projects/{projectId}")
    public String updateProject(
            @PathVariable Integer projectId, @Valid @RequestBody ProjectUpdateRequest request) {
        projectService.updateProject(projectId, request);
        return "Project updated successfully";
    }
}
