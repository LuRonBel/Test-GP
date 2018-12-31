package com.patskevich.gpproject.repository;

import com.patskevich.gpproject.entity.NicknameChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NicknameChangeHistoryRepository extends JpaRepository<NicknameChangeHistory, Long> {
}
