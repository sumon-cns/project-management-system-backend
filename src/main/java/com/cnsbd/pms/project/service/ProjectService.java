package com.cnsbd.pms.project.service;

import com.cnsbd.pms.project.dto.ProjectUpdateRequest;
import com.cnsbd.pms.project.dto.ProjectDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    void createProject(ProjectDto dto);

    List<ProjectDto> getAllProjects(Integer userId);

    void addUsers(Integer projectId, List<Integer> users);

    List<ProjectDto> getAllProjects(Integer userId, LocalDateTime fromDate, LocalDateTime toDate);

    void deleteProject(Integer projectId);

    void updateProject(Integer projectId, ProjectUpdateRequest request);

    ProjectDto getProject(Integer projectId);

    byte[] getReport(Integer userId, LocalDateTime fromDate, LocalDateTime toDate);
}
