package com.cnsbd.pms.project.controller;

import com.cnsbd.pms.project.dto.ProjectDto;
import com.cnsbd.pms.project.dto.ProjectUpdateRequest;
import com.cnsbd.pms.project.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@Tag(name = "Project Controller", description = "project related api(s)")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(
            summary = "Fetch all projects",
            description = "fetches all projects and their data from data source")
    @GetMapping(value = "/users/{userId}/projects/all")
    public List<ProjectDto> getAllProjects(@PathVariable Integer userId) {
        return projectService.getAllProjects(userId);
    }

    @Operation(summary = "Create project", description = "create a new project")
    @PostMapping(value = "/projects")
    public String createProject(@Valid @RequestBody ProjectDto dto) {
        projectService.createProject(dto);
        return "Project created successfully";
    }

    @Operation(summary = "Add member", description = "add member to an existing project")
    @PutMapping(value = "/projects/{projectId}/users")
    public String addUsers(@PathVariable Integer projectId, @RequestBody List<Integer> users) {
        projectService.addUsers(projectId, users);
        return "User(s) added successfully";
    }

    @Operation(
            summary = "Get all projects",
            description = "get all projects for specific user and a date range")
    @GetMapping(value = "/users/{userId}/projects")
    public List<ProjectDto> getAllProjects(
            @PathVariable Integer userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime toDate) {
        return projectService.getAllProjects(userId, fromDate, toDate);
    }

    @Operation(summary = "Delete project", description = "delete a project")
    @DeleteMapping(value = "/projects/{projectId}")
    public String deleteProject(@PathVariable Integer projectId) {
        projectService.deleteProject(projectId);
        return "Project deleted successfully";
    }

    @Operation(summary = "Update project", description = "update an existing project")
    @PutMapping(value = "/projects/{projectId}")
    public String updateProject(
            @PathVariable Integer projectId, @Valid @RequestBody ProjectUpdateRequest request) {
        projectService.updateProject(projectId, request);
        return "Project updated successfully";
    }

    @Operation(summary = "Get specific project", description = "get an existing project by id")
    @GetMapping(value = "/projects/{projectId}")
    public ProjectDto getProject(@PathVariable Integer projectId) {
        return projectService.getProject(projectId);
    }

    @Operation(summary = "Download report", description = "download report in pdf format")
    @GetMapping(value = "/users/{userId}/projects/report")
    public ResponseEntity<byte[]> getProjectReport(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime toDate) {
        byte[] reportBytes = projectService.getReport(userId, fromDate, toDate);

        String filename =
                "projects_report_"
                        + LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"))
                        + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(reportBytes.length);
        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }
}
