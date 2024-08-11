package com.cnsbd.pms.pmuser.service.impl;

import com.cnsbd.pms.exception.ProjectNotFoundException;
import com.cnsbd.pms.pmuser.dto.PmUserDto;
import com.cnsbd.pms.pmuser.entity.PmUser;
import com.cnsbd.pms.pmuser.repository.PmUserRepository;
import com.cnsbd.pms.pmuser.service.PmUserService;
import com.cnsbd.pms.project.entity.Project;
import com.cnsbd.pms.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PmUserServiceImpl implements PmUserService {
    private final PmUserRepository pmUserRepository;
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;

    @Override
    public void savePmUser(PmUser pmUser) {
        pmUserRepository.save(pmUser);
    }

    @Override
    public Optional<PmUser> getPmUser(String username) {
        return pmUserRepository.findByUsername(username);
    }

    @Override
    public List<PmUserDto> getAllUsers() {
        return pmUserRepository.findAll().stream()
                .map(pmUser -> modelMapper.map(pmUser, PmUserDto.class))
                .toList();
    }

    @Override
    public List<PmUserDto> getAvailableUsers(Integer projectId) {
        String message = "No project found with id: " + projectId + " to load available member(s)";
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(() -> new ProjectNotFoundException(message));
        List<PmUser> existingUsers = pmUserRepository.findUsersByProjectId(projectId);
        List<PmUser> users =
                pmUserRepository.findAll().stream()
                        .filter(u -> !Objects.equals(u.getId(), project.getOwner().getId()))
                        .filter(u -> !existingUsers.contains(u))
                        .toList();
        return users.stream().map(u -> modelMapper.map(u, PmUserDto.class)).toList();
    }
}
