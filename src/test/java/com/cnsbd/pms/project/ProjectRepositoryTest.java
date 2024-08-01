package com.cnsbd.pms.project;

import static org.junit.jupiter.api.Assertions.*;

import com.cnsbd.pms.pmuser.PmUser;
import com.cnsbd.pms.pmuser.PmUserRepository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Disabled
@SpringBootTest
class ProjectRepositoryTest {
    @Autowired private ProjectRepository projectRepository;
    @Autowired private PmUserRepository pmUserRepository;

    @Test
    @Transactional
    void findAllByMembersInOrOwnerIs() {
        PmUser pmUser = pmUserRepository.findById(1).orElse(null);
        assert pmUser != null;
        List<Project> projects =
                projectRepository.findAllByMembersContainingOrOwnerIsAndStartDateTimeBetween(
                        pmUser,
                        pmUser,
                        LocalDateTime.of(2024, 1, 1, 1, 10),
                        LocalDateTime.now().plusYears(1));
        System.out.println(
                "all project this user is: " + projects.stream().map(Project::getId).toList());
    }
}
