package com.cnsbd.pms.project;

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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime toDate) {
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

    @GetMapping(value = "/projects/{projectId}")
    public ProjectDto getProject(@PathVariable Integer projectId) {
        return projectService.getProject(projectId);
    }

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
