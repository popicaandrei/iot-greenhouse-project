package com.iot.greenhouse.service;

import com.iot.greenhouse.client.WeatherClient;
import com.iot.greenhouse.messaging.EventPayload;
import com.iot.greenhouse.model.CommandLog;
import com.iot.greenhouse.model.DesiredState;
import com.iot.greenhouse.model.GreenhouseMonitor;
import com.iot.greenhouse.model.WeatherApiDto;
import com.iot.greenhouse.repository.CommandLogRepository;
import com.iot.greenhouse.repository.DesiredStateRepository;
import com.iot.greenhouse.repository.GreenhouseRepository;
import com.iot.greenhouse.util.EventMapper;
import com.iot.greenhouse.util.WeatherMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.iot.greenhouse.util.Constants.LATITUDE;
import static com.iot.greenhouse.util.Constants.LONGITUDE;
import static com.iot.greenhouse.util.Constants.UNITS;

@Service
@RequiredArgsConstructor
@Slf4j
public class GreenhouseService {

    public final GreenhouseRepository greenhouseRepository;
    public final CommandLogRepository commandLogRepository;
    public final DesiredStateRepository desiredStateRepository;
    private final WeatherClient weatherClient;

    @Value("${weather.apiKey}")
    private String apiKey;

    public GreenhouseMonitor saveMonitor(GreenhouseMonitor monitor) {
        return greenhouseRepository.save(monitor);
    }

    public DesiredState saveDesiredState(DesiredState desiredState) {
        return desiredStateRepository.save(desiredState);
    }

    public CommandLog saveLog(CommandLog commandLog) {
        return commandLogRepository.save(commandLog);
    }

    public List<GreenhouseMonitor> getAllMonitorsBefore(Date fromDate) {
        return greenhouseRepository.findAllByTimestampAfter(fromDate);
    }

    public GreenhouseMonitor getLastMonitor() {
        return greenhouseRepository.findLastRecord();
    }

    public List<CommandLog> getAllCommands() {
        return commandLogRepository.findAll();
    }

    public WeatherApiDto getWeatherInfo() {
        log.info("Getting the weather from external API");
        return WeatherMapper.convertToDto(weatherClient.getCurrentWeather(LATITUDE, LONGITUDE, this.apiKey, UNITS));
    }

    public List<WeatherApiDto> getWeatherForecast() {
        log.info("Getting the forecast from external API for next 5 days");
        return WeatherMapper.convertToForecastDtoList(weatherClient.getWeatherForecast(LATITUDE, LONGITUDE, this.apiKey, UNITS));
    }


    @Transactional
    public void interpretMonitorData(EventPayload monitorEvent) {
        GreenhouseMonitor monitor = EventMapper.convertMessagePayload(monitorEvent);
        saveMonitor(monitor);
    }
}