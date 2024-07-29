package com.cnsbd.pms.pmuser;

import lombok.Data;

@Data
public class PmUserDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
}
