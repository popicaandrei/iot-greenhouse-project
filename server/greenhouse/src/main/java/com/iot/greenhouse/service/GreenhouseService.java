package com.iot.greenhouse.service;

import com.iot.greenhouse.messaging.EventPayload;
import com.iot.greenhouse.model.GreenhouseMonitor;
import com.iot.greenhouse.repository.GreenhouseRepository;
import com.iot.greenhouse.util.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GreenhouseService {

    public final GreenhouseRepository greenhouseRepository;

    public GreenhouseMonitor saveMonitor(GreenhouseMonitor monitor) {
        return greenhouseRepository.save(monitor);
    }

    public List<GreenhouseMonitor> getAllMonitorsBefore(Date fromDate){
        return greenhouseRepository.findAllByTimestampAfter(fromDate);
    }

    public void interpretMonitorData(EventPayload monitorEvent){
        GreenhouseMonitor greenhouseMonitor = EventMapper.convertMessagePayload(message.getPayload());

        saveMonitor()
    }
}
