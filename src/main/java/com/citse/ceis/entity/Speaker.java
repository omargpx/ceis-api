package com.citse.ceis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "TAX_SPEAKERS")
public class Speaker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_speaker")
    private int id;
    @Column(name = "no_name")
    private String name;
    @Column(name = "no_degree")
    private String degree;
    @Column(name = "no_function")
    private String function;
    @Column(name = "url_profile")
    private String profile;
    @Column(name = "url_img")
    private String image;

    @JsonIgnore
    @ManyToMany(mappedBy = "speakers")
    private Set<Event> events;
}
