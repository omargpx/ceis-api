package com.citse.ceis.repository;

import com.citse.ceis.entity.Event;
import com.citse.ceis.entity.Nodevice;
import com.citse.ceis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeviceDao extends JpaRepository<Nodevice,Integer> {
    Nodevice findByCode(String code);
    @Query("SELECT COUNT(n) > 0 FROM Nodevice n WHERE n.userN = :user AND n.eventN = :event")
    boolean existsByUserAndEvent(@Param("user") User user, @Param("event") Event event);
    Optional<Nodevice> findByUserNAndEventN(User user, Event event);
}
