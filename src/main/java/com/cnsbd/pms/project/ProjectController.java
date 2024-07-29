package com.cnsbd.pms.project;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping(value = "/projects")
    public List<ProjectDto> getAllProjects() {
        ProjectDto dto = new ProjectDto();
        dto.setId(1);
        dto.setName("Demo Project 1");
        return List.of(dto);
    }
}
