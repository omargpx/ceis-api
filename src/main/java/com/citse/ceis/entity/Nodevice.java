package com.citse.ceis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tax_nodevice")
public class Nodevice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gencode")
    private int id;
    @Column(name = "alf_code")
    private String code;
    @Column(name = "is_used")
    private Boolean isUsed;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userN;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event eventN;
}
