package com.iot.greenhouse.repository;

import com.iot.greenhouse.model.GreenhouseMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface GreenhouseRepository extends JpaRepository<GreenhouseMonitor, Integer> {

    List<GreenhouseMonitor> findAllByTimestampAfter(Date timeBefore);

    @Query("SELECT DISTINCT g FROM GreenhouseMonitor g ORDER BY g.timestamp DESC LIMIT 1")
    Optional<GreenhouseMonitor> findLastRecord();
}
