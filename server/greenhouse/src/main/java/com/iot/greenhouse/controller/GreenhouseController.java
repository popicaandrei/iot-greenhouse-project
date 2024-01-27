package com.iot.greenhouse.controller;

import com.iot.greenhouse.client.WeatherApiPayload;
import com.iot.greenhouse.model.GreenhouseMonitor;
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
@RequiredArgsConstructor
@Slf4j
public class GreenhouseController {

    public final GreenhouseService greenhouseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void submitForm(@RequestBody GreenhouseMonitor greenhouseMonitor) {
        greenhouseService.saveMonitor(greenhouseMonitor);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GreenhouseMonitor> getAllForms(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate) {
       return greenhouseService.getAllMonitorsBefore(fromDate);
    }

    @GetMapping("/weather")
    @ResponseStatus(HttpStatus.OK)
    public WeatherApiPayload getWeatherInfo() {
        return greenhouseService.getWeatherInfo();
    }
}
