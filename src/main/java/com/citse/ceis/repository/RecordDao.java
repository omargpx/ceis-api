package com.citse.ceis.repository;

import com.citse.ceis.entity.Event;
import com.citse.ceis.entity.Record;
import com.citse.ceis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordDao extends JpaRepository<Record,Integer> {
    Optional<Record> findByUserAndEvent(User user, Event event);
    @Query("SELECT r FROM Record r " +
            "JOIN r.event e " +
            "JOIN r.user u " +
            "WHERE u.dni = :dni " +
            "AND r.confirm = false ")
    List<Record> findUnconfirmedRecordsByUserDniAndEventType(@Param("dni") String dni);
    @Query("SELECT r FROM Record r WHERE r.event.typeEvent = 'COLOQUIO' OR r.event.typeEvent = 'SESIONES_PARALELAS'")
    List<Record> findAllByEvents();
}
