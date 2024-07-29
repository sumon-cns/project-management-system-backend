package com.cnsbd.pms.project;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDto {
    private Integer id;
    private String name;
    private String intro;
    private ProjectStatus status;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
