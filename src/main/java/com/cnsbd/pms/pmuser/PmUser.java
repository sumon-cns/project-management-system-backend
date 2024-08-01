package com.cnsbd.pms.pmuser;

import com.cnsbd.pms.project.Project;

import jakarta.persistence.*;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pm_users")
@ToString(exclude = "projects")
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

    @ManyToMany(mappedBy = "members")
    private List<Project> projects = new ArrayList<>();
}
