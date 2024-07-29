package com.cnsbd.pms.pmuser;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pm_users")
public class PmUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;
}
