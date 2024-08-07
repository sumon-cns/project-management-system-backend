package com.cnsbd.pms.pmuser.service;

import com.cnsbd.pms.pmuser.dto.PmUserDto;
import com.cnsbd.pms.pmuser.entity.PmUser;

import java.util.List;
import java.util.Optional;

public interface PmUserService {
    void savePmUser(PmUser pmUser);

    Optional<PmUser> getPmUser(String username);

    List<PmUserDto> getAllUsers();

    List<PmUserDto> getAvailableUsers(Integer projectId);
}
