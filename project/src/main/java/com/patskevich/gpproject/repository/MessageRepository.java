package com.patskevich.gpproject.repository;


import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRoom(Room room);
    List<Message> findAllByRoomOrderByIdDesc(Room room);
    List<Message> findByRoomAndDateBetween(Room room, final Date fromDate, final Date toDate);
    void deleteAllByRoom(Room room);
    Message findByRoom(Room byName);
}
