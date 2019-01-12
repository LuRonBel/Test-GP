package com.patskevich.gpproject.repository;

import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByLogin(final String name);
    boolean existsByLogin(final String name);
    List<User> findAllByRoom(final Room room);
}
