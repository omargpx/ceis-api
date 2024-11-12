package com.citse.ceis.utils.contracts;

import com.citse.ceis.entity.Event;
import com.citse.ceis.entity.Record;

import java.util.List;

public interface RecordService {
    List<Record> getAll();
    Record getById(int id);
    Record save(String dni, String eventCode);
    Record confirmStamp(String dni, String eventCode);
    Record save(String genCode);
    void deleteById(int id);
    //FILTERS
    List<Event> getRecordsByUser(String dni);
    List<Event> getRecordsByUserAndDay(String dni, Integer day);

    Object saveAll(String dni, List<Event> events);
}
