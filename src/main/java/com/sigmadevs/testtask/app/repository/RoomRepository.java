package com.sigmadevs.testtask.app.repository;

import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
