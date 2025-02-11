package com.sigmadevs.testtask.app.repository;

import com.sigmadevs.testtask.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTasksByQuest_Id(Long id);
}
