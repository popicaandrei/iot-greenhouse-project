package com.iot.greenhouse.util;

import com.iot.greenhouse.messaging.EventPayload;
import com.iot.greenhouse.model.GreenhouseMonitor;

public class EventMapper {

    public static GreenhouseMonitor convertMessagePayload(EventPayload eventPayload) {
        return GreenhouseMonitor.builder()
                .temperature(eventPayload.getTemperature())
                .humidity(eventPayload.getHumidity())
                .build();
    }
}
