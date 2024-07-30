package com.cnsbd.pms.project;

import com.cnsbd.pms.exceptionhandler.BadRequestException;
import com.cnsbd.pms.exceptionhandler.ProjectNotFoundException;
import com.cnsbd.pms.pmuser.PmUser;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

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
        users.forEach(
                id -> {
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
        projectRepository.delete(
                projectRepository
                        .findById(projectId)
                        .orElseThrow(
                                () ->
                                        new BadRequestException(
                                                "No project found with id: " + projectId)));
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
        if (StringUtils.hasText(request.getName())) {
            project.setName(request.getName());
        }
        if (StringUtils.hasText(request.getIntro())) {
            project.setIntro(request.getIntro());
        }
        if (request.getProjectStatus() != null) {
            project.setProjectStatus(request.getProjectStatus());
        }
        if (request.getStartDateTime() != null) {
            project.setStartDateTime(request.getStartDateTime());
        }
        if (request.getEndDateTime() != null) {
            project.setEndDateTime(request.getEndDateTime());
        }
        projectRepository.save(project);
    }

    private ProjectDto mapProjectToDto(Project project) {
        ProjectDto dto = modelMapper.map(project, ProjectDto.class);
        dto.setStartDateTime(project.getStartDateTime());
        dto.setEndDateTime(project.getEndDateTime());

        dto.setOwnerId(project.getOwner().getId());
        dto.setMemberIds(project.getMembers().stream().map(PmUser::getId).toList());
        return dto;
    }
}
