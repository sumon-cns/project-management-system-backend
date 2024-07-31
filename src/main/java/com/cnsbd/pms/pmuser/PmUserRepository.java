package com.cnsbd.pms.pmuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PmUserRepository extends JpaRepository<PmUser, Integer> {
    Optional<PmUser> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(
            value =
                    """
                            SELECT u.*
                            FROM pm_users u
                            WHERE u.id NOT IN (SELECT DISTINCT pu.pm_user_id
                                               FROM projects p
                                                        LEFT JOIN project_pm_user pu ON p.id = pu.project_id
                                               WHERE p.id = :projectId)
                              AND u.id NOT IN (SELECT proj.owner_id from projects proj WHERE proj.id = :projectId)
                            """,
            nativeQuery = true)
    List<PmUser> findAvailableUsers(@Param("projectId") Integer projectId);
}
