package com.citse.ceis.utils.logic;

import com.citse.ceis.entity.Event;
import com.citse.ceis.entity.Nodevice;
import com.citse.ceis.entity.Record;
import com.citse.ceis.entity.User;
import com.citse.ceis.exceptions.GUSException;
import com.citse.ceis.models.EventType;
import com.citse.ceis.repository.NodeviceDao;
import com.citse.ceis.repository.RecordDao;
import com.citse.ceis.utils.contracts.EventService;
import com.citse.ceis.utils.contracts.RecordService;
import com.citse.ceis.utils.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordImp implements RecordService {

    @Autowired
    private RecordDao repo;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private NodeviceDao nodeviceDao;

    @Override
    public List<Record> getAll() {
        List<Record> records = repo.findAllByEvents();
        if (!records.isEmpty())
            return records;
        throw new GUSException("RECORD_SERVICE", null, HttpStatus.NO_CONTENT);
    }

    @Override
    public Record getById(int id) {
        Record record = repo.findById(id).orElse(null);
        if(null!=record)
            return record;
        throw new GUSException("RECORD_SERVICE", null, HttpStatus.NOT_FOUND);
    }

    @Override
    public Record save(String dni, String eventCode) {
        LocalDateTime date = LocalDateTime.now();
        User user = userService.findByDni(dni);
        Event event = eventService.findByCode(eventCode);
        verifyRecord(user, event);
        updateQuota(event);
        Record record = new Record();
        record.setDate(date);
        record.setUser(user);
        record.setEvent(event);
        return repo.save(record);
    }


    @Override
    public Record confirmStamp(String dni, String eventCode) {
      User user = userService.findByDni(dni);
      Event event = eventService.findByCode(eventCode);

      if (event.getTypeEvent() == EventType.CHARLA_MAGISTRAL || event.getTypeEvent() == EventType.GENERAL) {
           var existingRecord = repo.findByUserAndEvent(user, event);
           if (existingRecord.isPresent()) {
             throw new GUSException("RECORD_SERVICE", "Record already exists", HttpStatus.NOT_FOUND);
           }
        
           Record newRecord = new Record();
           newRecord.setDate(LocalDateTime.now());
           newRecord.setUser(user);
           newRecord.setEvent(event);
           newRecord.setConfirm(true);
           return repo.save(newRecord);
        }

        var existingConfirmedRecord = repo.findByUserAndEvent(user, event);
        if (existingConfirmedRecord.isEmpty()) {
           throw new GUSException("RECORD_SERVICE", "Record doesn't exist", HttpStatus.NOT_FOUND);
        }
    
        existingConfirmedRecord.get().setConfirm(true);
        return repo.save(existingConfirmedRecord.get());
    } 


    @Override
    public Record save(String genCode) {
        LocalDateTime date = LocalDateTime.now();
        Nodevice n = nodeviceDao.findByCode(genCode);
        User user = userService.getById(n.getUserN().getId());
        Event event = eventService.getById(n.getEventN().getId());
        verifyRecord(user, event);
        n.setIsUsed(true);
        nodeviceDao.save(n);
        Record record = new Record();
        record.setDate(date);
        record.setUser(user);
        record.setEvent(event);
        record.setConfirm(true);
        return repo.save(record);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Override
    public List<Event> getRecordsByUser(String dni) {
        return repo.findAllByUser(userService.findByDni(dni)).stream()
                .map(Record::getEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getRecordsByUserAndDay(String dni, Integer day) {
        var events = getRecordsByUser(dni);
        return events.stream()
                .filter(event -> event.getDate().getDayOfMonth() == day)
                .toList();
    }

    @Override
    public Object saveAll(String dni, List<Event> events) {
        var day = LocalDateTime.now().getDayOfMonth();
        if(events.size()>4)
            throw new GUSException("RECORD_SERVICE", "record limit exceeded", HttpStatus.BAD_REQUEST);
        User user = userService.findByDni(dni);
        List<Record> records = new ArrayList<>();
        for (Event event: events){
            if ((day == 12 && event.getTypeEvent() != EventType.COLOQUIO) || ((day == 13 || day == 14) && event.getTypeEvent() != EventType.SESIONES_PARALELAS))
                continue;
            var existingRecord = repo.findByUserAndEvent(user,event);
            if (existingRecord.isPresent())
                continue;
            updateQuota(event);
            Record record = new Record();
            record.setDate(LocalDateTime.now());
            record.setUser(user);
            record.setEvent(event);
            records.add(repo.save(record));
        }
        return "total records success: "+records.size();
    }

    private void verifyRecord(User user, Event event){
        var record = repo.findByUserAndEvent(user,event);
        if(record.isEmpty())
            throw new GUSException("RECORD_SERVICE", "Record doesn't exists", HttpStatus.valueOf(300));
    }

    private void updateQuota(Event event){
        var quota = event.getQuota();
        if(quota>=1){
            event.setQuota(event.getQuota()-1);
            eventService.update(event);
        }
        if(quota==0)
            throw new GUSException("RECORD_SERVICE", "Sin cupos disponibles", HttpStatus.BAD_REQUEST);
    }
}
