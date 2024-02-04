package com.iot.greenhouse.controller;

import com.iot.greenhouse.model.CommandLog;
import com.iot.greenhouse.model.DesiredState;
import com.iot.greenhouse.model.GreenhouseMonitor;
import com.iot.greenhouse.model.WeatherApiDto;
import com.iot.greenhouse.service.GreenhouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/greenhouse")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class GreenhouseController {

    public final GreenhouseService greenhouseService;

    @GetMapping("/monitors")
    @ResponseStatus(HttpStatus.OK)
    public List<GreenhouseMonitor> getGreenhouseMonitors(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate) {
       return greenhouseService.getAllMonitorsBefore(fromDate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GreenhouseMonitor getGreenhouseCurrentState() {
        return greenhouseService.getLastMonitor();
    }

    @GetMapping("/commands")
    @ResponseStatus(HttpStatus.OK)
    public List<CommandLog> getCommandLogs() {
        return greenhouseService.getAllCommands();
    }

    @GetMapping("/weather")
    @ResponseStatus(HttpStatus.OK)
    public WeatherApiDto getWeatherInfo() {
        return greenhouseService.getWeatherInfo();
    }

    @GetMapping("/weather/forecast")
    @ResponseStatus(HttpStatus.OK)
    public List<WeatherApiDto> getWeatherForecast() {
        return greenhouseService.getWeatherForecast();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveDesiredState(@RequestBody DesiredState desiredState) {
        greenhouseService.saveDesiredState(desiredState);
    }

}
