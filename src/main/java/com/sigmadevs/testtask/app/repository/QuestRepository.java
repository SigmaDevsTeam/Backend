package com.sigmadevs.testtask.app.repository;

import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
}
