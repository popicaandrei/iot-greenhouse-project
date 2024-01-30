package com.iot.greenhouse.repository;

import com.iot.greenhouse.model.DesiredState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesiredStateRepository extends JpaRepository<DesiredState, Integer> {

    @Query("SELECT g FROM GreenhouseMonitor g ORDER BY g.timestamp DESC")
    Optional<DesiredState> findLastDesiredState();
}
