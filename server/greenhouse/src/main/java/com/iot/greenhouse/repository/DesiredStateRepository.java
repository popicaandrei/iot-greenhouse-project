package com.iot.greenhouse.repository;

import com.iot.greenhouse.model.DesiredState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesiredStateRepository extends JpaRepository<DesiredState, Integer> {
}
