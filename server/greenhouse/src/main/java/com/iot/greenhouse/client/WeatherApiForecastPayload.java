package com.iot.greenhouse.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiForecastPayload {

    private String cod;
    private int message;
    private int cnt;
    private City city;
    private List<WeatherApiForecastData> list;

    @Data
    public static class City {
        private int id;
        private String name;
        private Coord coord;
        private String country;
        private int population;
        private int timezone;
        private long sunrise;
        private long sunset;

        @Data
        public static class Coord {
            private double lat;
            private double lon;
        }
    }

    @Data
    public static class WeatherApiForecastData {
        private long dt;
        private WeatherApiPayload.Main main;
        private WeatherApiPayload.Weather[] weather;
        private WeatherApiPayload.Clouds clouds;
        private WeatherApiPayload.Wind wind;
        private int visibility;
        private double pop;
        private WeatherApiPayload.Sys sys;
        private String dt_txt;
    }
}