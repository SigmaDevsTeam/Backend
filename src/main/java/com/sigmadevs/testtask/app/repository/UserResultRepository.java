package com.sigmadevs.testtask.app.repository;

import com.sigmadevs.testtask.app.entity.UserResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResultRepository extends JpaRepository<UserResult, Long> {
}
