package com.iot.greenhouse.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherApiDto {

    private String condition;
    private String description;
    private double temp;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private int pressure;
    private int humidity;
    private long sunrise;
    private long sunset;
    private double windSpeed;
    private int clouds;
    private String locationName;
    private String timestamp;
}
