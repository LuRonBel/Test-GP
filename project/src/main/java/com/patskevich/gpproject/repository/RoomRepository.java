package com.patskevich.gpproject.repository;

import com.patskevich.gpproject.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String name);
    boolean existsByName (String name);
}
