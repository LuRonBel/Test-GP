package com.patskevich.gpproject.repository;


import com.patskevich.gpproject.entity.Message;
import com.patskevich.gpproject.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findAllByRoom(final Room room);
    public List<Message> findAllByRoomOrderByIdDesc(final Room room);
    public void deleteAllByRoom(final Room room);
    public Message findByRoom(final Room byName);
    public Message getById(final Long id);
}
