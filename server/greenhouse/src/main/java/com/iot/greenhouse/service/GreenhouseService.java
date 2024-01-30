package com.iot.greenhouse.service;

import com.iot.greenhouse.client.WeatherClient;
import com.iot.greenhouse.messaging.EventPayload;
import com.iot.greenhouse.messaging.RabbitClient;
import com.iot.greenhouse.model.*;
import com.iot.greenhouse.repository.CommandLogRepository;
import com.iot.greenhouse.repository.DesiredStateRepository;
import com.iot.greenhouse.repository.GreenhouseRepository;
import com.iot.greenhouse.util.EventMapper;
import com.iot.greenhouse.util.WeatherMapper;
import jakarta.persistence.EntityNotFoundException;
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
    private final RabbitClient rabbitClient;

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

    public DesiredState getLatestDesiredState() {
        return desiredStateRepository.findLastDesiredState()
                .orElseThrow(() -> new EntityNotFoundException("Latest state not found"));
    }

    public List<GreenhouseMonitor> getAllMonitorsBefore(Date fromDate) {
        return greenhouseRepository.findAllByTimestampAfter(fromDate);
    }

    public GreenhouseMonitor getLastMonitor() {
        return greenhouseRepository.findLastRecord()
                .orElseThrow(() -> new EntityNotFoundException("Latest monitor not found"));
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
        GreenhouseMonitor currentState = EventMapper.convertMessagePayload(monitorEvent);
        DesiredState desiredState = this.getLatestDesiredState();

        boolean command = calculateCommand(desiredState, currentState);
        rabbitClient.sendToFeedbackTopic(new SwitchEvent(command));

        saveMonitor(currentState);
        saveLog(new CommandLog(command));
    }

    private boolean calculateCommand(DesiredState desiredState, GreenhouseMonitor currentState) {
        Double diff = Math.abs(desiredState.getTemperature() - currentState.getTemperature());

        if (diff >= 3) return true;
        else if (diff <= 0.5) return false;
        return false;
    }
}