package com.patskevich.gpproject.repository;

import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByLogin(String name);
    User findByNickname(String nickname);
    boolean existsByLogin (final String name);
    boolean existsByNickname(String name);
    List<User> findAllByRoom(Room room);
    User getById(long id);
}
