package com.cnsbd.pms.project.entity;

import com.cnsbd.pms.pmuser.entity.PmUser;
import com.cnsbd.pms.project.enums.ProjectStatus;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "intro")
    private String intro;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PmUser owner;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;
}
