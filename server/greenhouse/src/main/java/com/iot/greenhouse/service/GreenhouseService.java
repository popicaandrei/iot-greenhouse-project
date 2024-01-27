package com.iot.greenhouse.service;

import com.iot.greenhouse.client.WeatherApiPayload;
import com.iot.greenhouse.client.WeatherClient;
import com.iot.greenhouse.messaging.EventPayload;
import com.iot.greenhouse.model.GreenhouseMonitor;
import com.iot.greenhouse.repository.GreenhouseRepository;
import com.iot.greenhouse.util.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GreenhouseService {

    public final GreenhouseRepository greenhouseRepository;
    private final WeatherClient weatherClient;

    @Value("${weather.apiKey}")
    private String apiKey;

    public GreenhouseMonitor saveMonitor(GreenhouseMonitor monitor) {
        return greenhouseRepository.save(monitor);
    }

    public List<GreenhouseMonitor> getAllMonitorsBefore(Date fromDate) {
        return greenhouseRepository.findAllByTimestampAfter(fromDate);
    }

    public WeatherApiPayload getWeatherInfo() {
        log.info("Getting the data from external API");
        WeatherApiPayload =  weatherClient.getCurrentWeather("46.7712", "23.6236", this.apiKey, "metric");
    }

    @Transactional
    public void interpretMonitorData(EventPayload monitorEvent) {
        GreenhouseMonitor monitor = EventMapper.convertMessagePayload(monitorEvent);
        saveMonitor(monitor);
    }
}