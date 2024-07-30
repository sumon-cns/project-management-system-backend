package com.cnsbd.pms.project;

import com.cnsbd.pms.pmuser.PmUser;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "project_pm_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "pm_user_id"))
    private List<PmUser> members = new ArrayList<>();
}
