package com.citse.ceis.repository;

import com.citse.ceis.entity.Event;
import com.citse.ceis.models.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDao extends JpaRepository<Event,Integer> {
    Event findByCode(String code);
    List<Event> findAllByStatusIsTrue();
    List<Event> findByNameContains(String name);
    List<Event> findByTypeEvent(EventType type);
}
