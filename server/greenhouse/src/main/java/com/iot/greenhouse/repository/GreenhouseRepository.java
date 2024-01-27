package com.iot.greenhouse.repository;

import com.iot.greenhouse.model.GreenhouseMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GreenhouseRepository extends JpaRepository<GreenhouseMonitor, Integer> {

    List<GreenhouseMonitor> findAllByTimestampAfter(Date timeBefore);
}