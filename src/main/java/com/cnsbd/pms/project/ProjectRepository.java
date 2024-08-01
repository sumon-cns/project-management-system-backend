package com.cnsbd.pms.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(
            value =
                    """
                            SELECT distinct p.*
                            FROM projects p
                            LEFT JOIN project_pm_user pu ON p.id = pu.project_id
                            WHERE p.owner_id = :userId OR pu.pm_user_id = :userId
                            """,
            nativeQuery = true)
    List<Project> findAllProjectsByUserId(@Param("userId") Integer userId);

    @Query(
            value =
                    """
                            SELECT distinct p.*
                                            FROM projects p
                                            LEFT JOIN project_pm_user pu ON p.id = pu.project_id
                                            WHERE (p.owner_id = :userId OR pu.pm_user_id = :userId)
                                              AND (p.start_date_time BETWEEN :start AND :end
                                               OR p.end_date_time BETWEEN :start AND :end)
                            """,
            nativeQuery = true)
    List<Project> findAllProjectsByUserIdAndBetweenDate(
            @Param("userId") Integer userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
