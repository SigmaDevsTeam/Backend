package com.sigmadevs.testtask.app.repository;

import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByQuest_Id(Long id);
}
