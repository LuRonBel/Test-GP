package com.patskevich.gpproject.repository;

import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByRoomOrderByIdDesc(final Room room);
    Message getById(final Long id);
    List<Message> findAllByRoom(final Room room);
    List<Message> findByRoomAndDateBetween(final Room room, final Date fromDate, final Date toDate);
}
