package com.cnsbd.pms.project.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    pre(0),
    start(1),
    end(3);

    private final int code;

    ProjectStatus(int code) {
        this.code = code;
    }
}
