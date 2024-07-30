package com.cnsbd.pms.pmuser;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PmUserServiceImpl implements PmUserService {
    private final PmUserRepository pmUserRepository;
    private final ModelMapper modelMapper;

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
}
