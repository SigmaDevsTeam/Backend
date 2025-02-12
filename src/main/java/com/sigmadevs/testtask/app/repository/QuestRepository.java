package com.sigmadevs.testtask.app.repository;

import com.sigmadevs.testtask.app.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findAllByUser_Id(Long userId);
}
