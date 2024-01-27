package com.iot.greenhouse.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Builder
public class EventPayload {

    private String eventName;
    private Double temperature;
    private Double humidity;
    private Date timestamp;
}
