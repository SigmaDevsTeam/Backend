package com.sigmadevs.testtask.app.repository;

import com.sigmadevs.testtask.app.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
