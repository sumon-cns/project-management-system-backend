package com.cnsbd.pms.pmuser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PmUserServiceImpl implements PmUserService {
    private final PmUserRepository pmUserRepository;

    @Override
    public void savePmUser(PmUser pmUser) {
        pmUserRepository.save(pmUser);
    }

    @Override
    public Optional<PmUser> getPmUser(String username) {
        return pmUserRepository.findByUsername(username);
    }
}
