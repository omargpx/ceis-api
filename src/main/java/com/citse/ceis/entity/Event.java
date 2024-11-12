package com.citse.ceis.entity;

import com.citse.ceis.models.EventType;
import com.citse.ceis.models.Modality;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "TMA_EVENTS")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private int id;
    @Column(name = "no_event")
    private String name;
    @Column(name = "fe_date")
    private LocalDateTime date;
    @Column(name = "fe_endate")
    private LocalDateTime endDate;
    @Column(name = "no_place")
    private String place;
    @Column(name = "alf_code")
    private String code;
    @Column(name = "ty_modality")
    @Enumerated(EnumType.STRING)
    private Modality modality;
    @Column(name = "ty_event")
    @Enumerated(EnumType.STRING)
    private EventType typeEvent;
    @Column(name = "is_open")
    private Boolean status; //1 if the event until realized ; 0 if was done
    @Column(name = "nu_quota")
    private int quota; // number of places available -- c.u.p.o.s

    @ManyToMany
    @JoinTable(name = "tmv_speaker_event", joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "speaker_id"))
    private List<Speaker> speakers;

    @ManyToOne
    @JoinColumn(name = "father_id",referencedColumnName = "id_event")
    private Event father;

    @JsonIgnore
    @OneToMany(mappedBy = "bEvent")
    private List<Booking> bookings;
    @JsonIgnore
    @OneToMany(mappedBy = "event")
    private List<Record> records;
    @JsonIgnore
    @OneToMany(mappedBy = "eventN")
    private List<Nodevice> nodevices;
}
