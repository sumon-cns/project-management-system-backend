package com.cnsbd.pms.project.repository;

import com.cnsbd.pms.project.entity.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(
            value =
                    """
            SELECT pr.*
            FROM projects pr
            WHERE (pr.owner_id = :userId
                OR :userId IN (SELECT pu.pm_user_id
                               FROM project_pm_user pu
                               WHERE pr.id = pu.project_id))
              AND (pr.start_date_time BETWEEN :from AND :to
                OR pr.end_date_time BETWEEN :from AND :to)
            """,
            nativeQuery = true)
    List<Project> findAllProjectsByUserAndBetweenDateV1(
            @Param("userId") Integer userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query(
            value = "select count(*) from project_pm_user where project_id=:projectId",
            nativeQuery = true)
    int countExistingUsers(@Param("projectId") Integer projectId);

    @Modifying
    @Query(
            value =
                    """
                            insert into project_pm_user(project_id, pm_user_id) values (:projectId, :userId)
                            """,
            nativeQuery = true)
    void addUserToProject(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    @Modifying
    @Query(
            value =
                    """
                            delete from project_pm_user pu where pu.project_id=:projectId
                            """,
            nativeQuery = true)
    void deleteAllUsersFromProject(@Param("projectId") Integer projectId);

    @Modifying
    @Query(
            value =
                    """
                            delete from project_pm_user pu where pu.project_id=:projectId and pu.pm_user_id=:userId
                            """,
            nativeQuery = true)
    void deleteUserFromProject(
            @Param("projectId") Integer projectId, @Param("userId") Integer userId);
}
