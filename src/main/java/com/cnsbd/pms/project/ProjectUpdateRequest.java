package com.cnsbd.pms.project;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectUpdateRequest {
    private String name;
    private String intro;
    private ProjectStatus projectStatus;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
