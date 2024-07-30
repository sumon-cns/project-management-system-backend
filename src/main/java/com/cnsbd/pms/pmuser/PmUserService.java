package com.cnsbd.pms.pmuser;

import java.util.List;
import java.util.Optional;

public interface PmUserService {
    void savePmUser(PmUser pmUser);

    Optional<PmUser> getPmUser(String username);

    List<PmUserDto> getAllUsers();
}
