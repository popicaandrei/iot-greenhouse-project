package com.iot.greenhouse.repository;

import com.iot.greenhouse.model.CommandLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandLogRepository extends JpaRepository<CommandLog, Integer> {
}
