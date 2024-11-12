package com.citse.ceis.utils.contracts;

import com.citse.ceis.entity.Event;
import com.citse.ceis.models.EventType;

import java.util.List;

public interface EventService {
    List<Event> getAll();
    Object getAllBySort();
    Event getById(int id);
    Event save(Event event);
    Event update(Event event);
    void deleteById(int id);

    //filters
    List<Event> findByName(String name);
    Event findByCode(String code);
    List<Event> findByType(EventType type, Integer day);
    List<Event> getEventsByDay(Integer day);
}
