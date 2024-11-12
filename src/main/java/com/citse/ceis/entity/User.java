package com.citse.ceis.entity;

import com.citse.ceis.models.EntityType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "TMA_USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int id;
    @Column(name = "no_fullname")
    private String name;
    @Column(name = "nu_dni")
    private String dni;
    @Column(name = "no_nickname")
    private String nickname;
    @Column(name = "ty_entity")
    @Enumerated(EnumType.STRING)
    private EntityType entity;
    @Column(name = "url_avatar")
    private String avatar;
    @Column(name = "fe_date")
    private LocalDateTime date;
    @Column(name = "is_opened")
    private Boolean isOpened; // device restriction
    @Column(name = "ty_gender")
    private String gender;
    @Column(name = "co_ceis")
    private String code;
    @Column(name = "rol")
    private String rol;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Record> records;

    @JsonIgnore
    @OneToMany(mappedBy = "userN")
    private List<Nodevice> nodevices;
}
