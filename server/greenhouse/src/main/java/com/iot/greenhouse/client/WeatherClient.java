package com.iot.greenhouse.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "OpenWeatherApi", url = "https://api.openweathermap.org/data/2.5/weather")
public interface WeatherClient {

    @GetMapping
    WeatherApiPayload getCurrentWeather(@RequestParam("lat") String latitude,
                               @RequestParam("lon") String longitude,
                               @RequestParam("appid") String apiKey,
                               @RequestParam("units") String units);
}
