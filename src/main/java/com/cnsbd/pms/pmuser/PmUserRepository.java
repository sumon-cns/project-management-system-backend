package com.cnsbd.pms.pmuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PmUserRepository extends JpaRepository<PmUser, Integer> {
    Optional<PmUser> findByUsername(String username);
}
