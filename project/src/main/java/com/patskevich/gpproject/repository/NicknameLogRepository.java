package com.patskevich.gpproject.repository;

import com.patskevich.gpproject.entity.NicknameLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NicknameLogRepository extends JpaRepository<NicknameLog, Long> {

    public List<NicknameLog> findAll();
    public List<NicknameLog> findAllByOrderByIdDesc();
}

