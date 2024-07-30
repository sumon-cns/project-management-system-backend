package com.cnsbd.pms.project;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDto {
    private Integer id;

    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "intro is required")
    private String intro;

    @Min(value = 1)
    @NotNull(message = "owner id is required")
    private Integer ownerId;

    @NotNull(message = "status is required")
    private ProjectStatus projectStatus;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<Integer> memberIds;
}
