package com.citse.ceis.utils.logic;

import com.citse.ceis.entity.Event;
import com.citse.ceis.exceptions.GUSException;
import com.citse.ceis.models.EventType;
import com.citse.ceis.repository.EventDao;
import com.citse.ceis.utils.contracts.EventService;
import com.citse.ceis.utils.contracts.GUSMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventImp  implements EventService {

    @Autowired
    private EventDao repo;
    @Autowired
    private GUSMethods gus;

    @Override
    public List<Event> getAll() {
        List<Event> events = repo.findAllByStatusIsTrue();
        if (!events.isEmpty())
            return events;
        throw new GUSException("EVENT_SERVICE", "Events is empty", HttpStatus.NO_CONTENT);
    }

    @Override
    public Object getAllBySort() {
        var events = getAll();

        // order by date
        var sortedEvents = events.stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList();

        // group by day
        var eventsGroupedByDay = sortedEvents.stream()
                .collect(Collectors.groupingBy(event -> event.getDate().getDayOfMonth()));

        // format
        var formattedEvents = new HashMap<>();
        int dayCount = 12;
        for (Map.Entry<Integer, List<Event>> entry : eventsGroupedByDay.entrySet()) {
            formattedEvents.put(dayCount++, entry.getValue());
        }

        return formattedEvents;
    }

    @Override
    public Event getById(int id) {
        Event event = repo.findById(id).orElse(null);
        if(null!=event)
            return event;
        throw new GUSException("EVENT_SERVICE", null, HttpStatus.NOT_FOUND);
    }

    @Override
    public Event save(Event event) {
        event.setCode(gus.genCodeWorkshop());
        return repo.save(event);
    }

    @Override
    public Event update(Event event) {
        return repo.save(event);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<Event> findByName(String name) {
        List<Event> events = repo.findByNameContains(name);
        if(!events.isEmpty())
            return events;
        throw new GUSException("EVENT_SERVICE", null, HttpStatus.NOT_FOUND);
    }

    @Override
    public Event findByCode(String code) {
        Event events = repo.findByCode(code);
        if(null!=events)
            return events;
        throw new GUSException("EVENT_SERVICE", "Event not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Event> findByType(EventType type, Integer day) {
        List<Event> events = repo.findByTypeEvent(type);
        if(events.isEmpty())
            throw new GUSException("EVENT_SERVICE", "no found events with this type "+ type, HttpStatus.NOT_FOUND);
        if(null==day)
            return events;
        return events.stream()
                .filter(event -> event.getDate().getDayOfMonth() == day)
                .toList();
    }

    @Override
    public List<Event> getEventsByDay(Integer day) {
        var events = getAll();
        return events.stream()
                .filter(event -> event.getDate().getDayOfMonth() == day)
                .toList();
    }

    private int transformDay(int day){
        return switch (day) {
            case 2 -> 13;
            case 3 -> 14;
            case 4 -> 15;
            default -> 12;
        };
    }
}
