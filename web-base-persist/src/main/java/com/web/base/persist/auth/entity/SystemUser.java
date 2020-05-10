package com.web.base.persist.auth.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dong
 * @date 2020/5/10
 */
@Data
@Entity
@Table(name = "system_user")
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String account;

    private String password;

    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date createdTime;

    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date updatedTime;
}
