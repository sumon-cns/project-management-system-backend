package com.cnsbd.pms.pmuser;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PmUserDetailsService implements UserDetailsService {
    private final PmUserService pmUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PmUser pmUser =
                pmUserService
                        .getPmUser(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(username, pmUser.getPassword(), List.of());
    }
}
