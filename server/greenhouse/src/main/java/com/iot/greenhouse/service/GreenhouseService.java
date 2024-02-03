package com.iot.greenhouse.service;

import com.iot.greenhouse.client.WeatherClient;
import com.iot.greenhouse.messaging.EventPayload;
import com.iot.greenhouse.messaging.RabbitClient;
import com.iot.greenhouse.model.*;
import com.iot.greenhouse.repository.CommandLogRepository;
import com.iot.greenhouse.repository.DesiredStateRepository;
import com.iot.greenhouse.repository.GreenhouseRepository;
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
        GreenhouseMonitor currentState = new GreenhouseMonitor(monitorEvent.getTemperature(), monitorEvent.getHumidity());
        DesiredState desiredState = this.getLatestDesiredState();
        log.info("Interpreting new data from greenhouse with temperature {} and humidity {}, desired temperature: {}", currentState.getTemperature(), currentState.getHumidity(), desiredState.getTemperature());

        CommandLog commandLog = calculateCommand(desiredState.getTemperature(), currentState.getTemperature());
        rabbitClient.sendToFeedbackTopic(new SwitchEvent(commandLog.isHeaterSwitch(), commandLog.isFanSwitch()));
        log.info("Sent new command to greenhouse: heater {}, fan {}", commandLog.isHeaterSwitch(), commandLog.isFanSwitch());

        saveMonitor(currentState);
        saveLog(commandLog);
        log.info("Saved new current state, temp: {}", currentState.getTemperature());
    }

    private CommandLog calculateCommand(Double desiredTemp, Double currentTemp) {
        boolean heaterSwitch = false;
        boolean fanSwitch = false;

        if (desiredTemp <= currentTemp) heaterSwitch = false;
        else if (desiredTemp >= currentTemp + 3) heaterSwitch = true;

        if (desiredTemp >= currentTemp) fanSwitch = false;
        else if (desiredTemp <= currentTemp - 3) fanSwitch = true;

        return new CommandLog(heaterSwitch, fanSwitch);
    }
}