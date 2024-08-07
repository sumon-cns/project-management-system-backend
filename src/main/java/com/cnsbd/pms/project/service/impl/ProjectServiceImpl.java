package com.cnsbd.pms.project.service.impl;

import com.cnsbd.pms.exception.BadRequestException;
import com.cnsbd.pms.exception.OperationNotAllowedException;
import com.cnsbd.pms.exception.ProjectNotFoundException;
import com.cnsbd.pms.exception.UserNotFoundException;
import com.cnsbd.pms.pmuser.entity.PmUser;
import com.cnsbd.pms.pmuser.dto.PmUserDto;
import com.cnsbd.pms.pmuser.repository.PmUserRepository;

import com.cnsbd.pms.project.dto.ProjectUpdateRequest;
import com.cnsbd.pms.project.dto.ProjectDto;
import com.cnsbd.pms.project.dto.ProjectReportDto;
import com.cnsbd.pms.project.entity.Project;
import com.cnsbd.pms.project.repository.ProjectRepository;
import com.cnsbd.pms.project.service.ProjectService;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final PmUserRepository pmUserRepository;

    @Override
    public void createProject(ProjectDto dto) {
        Project project = modelMapper.map(dto, Project.class);
        // model mapper is not mapping dates (?)
        project.setStartDateTime(dto.getStartDateTime());
        project.setEndDateTime(dto.getEndDateTime());

        if (dto.getMemberIds() != null) {
            dto.getMemberIds().stream()
                    .limit(5)
                    .forEach(
                            mId -> {
                                PmUser user = new PmUser();
                                user.setId(mId);
                                project.getMembers().add(user);
                            });
        }
        PmUser owner = new PmUser();
        owner.setId(dto.getOwnerId());
        project.setOwner(owner);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDto> getAllProjects(Integer userId) {
        return projectRepository.findAllProjectsByUserId(userId).stream()
                .map(this::mapProjectToDto)
                .toList();
    }

    @Override
    public void addUsers(Integer projectId, List<Integer> users) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(
                                () ->
                                        new ProjectNotFoundException(
                                                "No project found with id: " + projectId));
        int existingUsersCount = project.getMembers().size();
        if (existingUsersCount + users.size() > 5) {
            throw new BadRequestException("Can't add more than 5 users to a project.");
        }
        User principal =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PmUser pmUser = pmUserRepository.findByUsername(principal.getUsername()).orElse(null);
        if (pmUser != null && users.contains(pmUser.getId())) {
            throw new BadRequestException("Can't add owner as a member of this project.");
        }

        users.forEach(
                id -> {
                    if (project.getMembers().stream().anyMatch(it -> it.getId().equals(id))) {
                        throw new BadRequestException("Can't add existing member to this project.");
                    }
                    PmUser user = new PmUser();
                    user.setId(id);
                    project.getMembers().add(user);
                });
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDto> getAllProjects(
            Integer userId, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null && toDate == null) {
            LocalDate today = LocalDate.now();
            LocalDate firstDayOfMonth = today.withDayOfMonth(1);
            LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
            LocalDateTime firstDateTimeOfMonth = LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
            LocalDateTime lastDateTimeOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);

            fromDate = firstDateTimeOfMonth;
            toDate = lastDateTimeOfMonth;
        }
        return projectRepository
                .findAllProjectsByUserIdAndBetweenDate(userId, fromDate, toDate)
                .stream()
                .map(this::mapProjectToDto)
                .toList();
    }

    @Override
    public void deleteProject(Integer projectId) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(
                                () ->
                                        new BadRequestException(
                                                "No project found with id: " + projectId));
        allowProjectOwnerToModifyOrDeleteProject(project);
        projectRepository.delete(project);
    }

    @Override
    public void updateProject(Integer projectId, ProjectUpdateRequest request) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(
                                () ->
                                        new ProjectNotFoundException(
                                                "No project found with id: " + projectId));
        allowProjectOwnerToModifyOrDeleteProject(project);

        if (StringUtils.hasText(request.getName())) {
            project.setName(request.getName());
        }
        if (StringUtils.hasText(request.getIntro())) {
            project.setIntro(request.getIntro());
        }
        if (request.getProjectStatus() != null) {
            project.setProjectStatus(request.getProjectStatus());
        }

        project.setStartDateTime(request.getStartDateTime());

        if (request.getEndDateTime() != null) {
            project.setEndDateTime(request.getEndDateTime());
        }
        projectRepository.save(project);
    }

    @Override
    public ProjectDto getProject(Integer projectId) {
        return projectRepository
                .findById(projectId)
                .map(this::mapProjectToDto)
                .orElseThrow(
                        () ->
                                new ProjectNotFoundException(
                                        "No project found with id: " + projectId));
    }

    @Override
    @SneakyThrows
    public byte[] getReport(Integer userId, LocalDateTime fromDate, LocalDateTime toDate) {
        PmUserDto owner =
                pmUserRepository
                        .findById(userId)
                        .map(u -> modelMapper.map(u, PmUserDto.class))
                        .orElseThrow(
                                () ->
                                        new UserNotFoundException(
                                                "User not found with id: " + userId));
        List<ProjectReportDto> projects =
                getAllProjects(userId, fromDate, toDate).stream()
                        .map(this::mapProjectDtoToReportDto)
                        .toList();
        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(projects);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projects", datasource);

        parameters.put(
                "owner",
                Map.of(
                        "name",
                        owner.getFullName(),
                        "email",
                        owner.getEmail(),
                        "username",
                        owner.getUsername()));
        parameters.put("fromDate", fromDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
        parameters.put("toDate", toDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));

        @Cleanup InputStream is = this.getClass().getResourceAsStream("/report/template.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(is);
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private ProjectDto mapProjectToDto(Project project) {
        ProjectDto dto = modelMapper.map(project, ProjectDto.class);
        dto.setStartDateTime(project.getStartDateTime());
        dto.setEndDateTime(project.getEndDateTime());

        dto.setOwner(modelMapper.map(project.getOwner(), PmUserDto.class));

        dto.setOwnerId(project.getOwner().getId());
        dto.setMemberIds(project.getMembers().stream().map(PmUser::getId).toList());

        dto.setMembers(
                project.getMembers().stream()
                        .map(user -> modelMapper.map(user, PmUserDto.class))
                        .toList());
        return dto;
    }

    private ProjectReportDto mapProjectDtoToReportDto(ProjectDto projectDto) {
        ProjectReportDto reportDto = modelMapper.map(projectDto, ProjectReportDto.class);
        reportDto.setOwner(projectDto.getOwner().getFullName());
        reportDto.setStatus(projectDto.getProjectStatus().name());
        if (projectDto.getStartDateTime() != null) {
            reportDto.setStartDateTime(
                    projectDto
                            .getStartDateTime()
                            .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
        } else {
            reportDto.setStartDateTime("--");
        }
        reportDto.setEndDateTime(
                projectDto.getEndDateTime().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));

        reportDto.setMembers(
                projectDto.getMembers().isEmpty()
                        ? "No member"
                        : String.join(
                                ", ",
                                projectDto.getMembers().stream()
                                        .map(PmUserDto::getUsername)
                                        .toList()));

        return reportDto;
    }

    private void allowProjectOwnerToModifyOrDeleteProject(Project project) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PmUser loggedInUser = pmUserRepository.findByUsername(user.getUsername()).orElse(null);
        if (loggedInUser == null || !loggedInUser.getId().equals(project.getOwner().getId())) {
            throw new OperationNotAllowedException("You are not allowed to do this operation!");
        }
    }
}
