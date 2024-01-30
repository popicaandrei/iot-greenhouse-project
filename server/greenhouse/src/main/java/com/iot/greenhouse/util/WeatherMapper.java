package com.iot.greenhouse.util;

import com.iot.greenhouse.client.WeatherApiForecastPayload;
import com.iot.greenhouse.client.WeatherApiPayload;
import com.iot.greenhouse.model.WeatherApiDto;

import java.util.List;
import java.util.stream.Collectors;

public class WeatherMapper {

    public static WeatherApiDto convertToDto(WeatherApiPayload weatherApiPayload) {
        WeatherApiPayload.Main main = weatherApiPayload.getMain();
        WeatherApiPayload.Weather weather = weatherApiPayload.getWeather()[0];

        return WeatherApiDto.builder()
                .condition(weather.getMain())
                .description(weather.getDescription())
                .temp(main.getTemp())
                .feelsLike(main.getFeels_like())
                .tempMin(main.getTemp_min())
                .tempMax(main.getTemp_max())
                .pressure(main.getPressure())
                .humidity(main.getHumidity())
                .sunrise(weatherApiPayload.getSys().getSunrise())
                .sunset(weatherApiPayload.getSys().getSunset())
                .windSpeed(weatherApiPayload.getWind().getSpeed())
                .clouds(weatherApiPayload.getClouds().getAll())
                .locationName(weatherApiPayload.getName())
                .build();
    }

    private static WeatherApiDto convertToDto(WeatherApiForecastPayload.WeatherApiForecastData weatherApiForecastData, WeatherApiForecastPayload.City city) {
        WeatherApiPayload.Main main = weatherApiForecastData.getMain();
        WeatherApiPayload.Weather weather = weatherApiForecastData.getWeather()[0];

        return WeatherApiDto.builder()
                .condition(weather.getMain())
                .description(weather.getDescription())
                .temp(main.getTemp())
                .feelsLike(main.getFeels_like())
                .tempMin(main.getTemp_min())
                .tempMax(main.getTemp_max())
                .pressure(main.getPressure())
                .humidity(main.getHumidity())
                .sunrise(weatherApiForecastData.getSys().getSunrise())
                .sunset(weatherApiForecastData.getSys().getSunset())
                .windSpeed(weatherApiForecastData.getWind().getSpeed())
                .clouds(weatherApiForecastData.getClouds().getAll())
                .locationName(city.getName())
                .timestamp(weatherApiForecastData.getDt_txt())
                .build();
    }

    public static List<WeatherApiDto> convertToDtoList(List<WeatherApiPayload> weatherForecastList) {
        return weatherForecastList.stream().map(weatherApiPayload -> convertToDto(weatherApiPayload)).toList();
    }

    public static List<WeatherApiDto> convertToForecastDtoList(WeatherApiForecastPayload forecastData) {
        WeatherApiForecastPayload.City city = forecastData.getCity();
        return forecastData.getList().stream()
                .map(forecast-> convertToDto(forecast, city))
                .collect(Collectors.toList());
    }
}

