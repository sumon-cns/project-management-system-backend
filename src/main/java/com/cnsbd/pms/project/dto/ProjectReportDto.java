package com.cnsbd.pms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectReportDto {
    private Integer id;
    private String name;
    private String intro;
    private String status;
    private String owner;
    private String startDateTime;
    private String endDateTime;
    private String members;
}
