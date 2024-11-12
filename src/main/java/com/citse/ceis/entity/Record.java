package com.citse.ceis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TMV_RECORDS")
public class Record implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_record")
    private int id;
    @Column(name = "fe_date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnoreProperties({"father","speakers"})
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "confirm")
    private boolean confirm;
}
