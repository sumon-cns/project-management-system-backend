package com.cnsbd.pms.pmuser;

import com.cnsbd.pms.exceptionhandler.ProjectNotFoundException;
import com.cnsbd.pms.project.Project;
import com.cnsbd.pms.project.ProjectRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public List<PmUserDto> getAvailableUsers(Integer projectId) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        List<PmUser> users =
                pmUserRepository.findAll().stream()
                        .filter(u -> !Objects.equals(u.getId(), project.getOwner().getId()))
                        .filter(u -> !project.getMembers().contains(u))
                        .toList();
        return users.stream().map(u -> modelMapper.map(u, PmUserDto.class)).toList();
    }
}
