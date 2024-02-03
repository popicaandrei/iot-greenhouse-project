package com.iot.greenhouse.model;

import lombok.Data;

@Data
public class SwitchEvent {
    private boolean heaterSwitch;
    private boolean fanSwitch;

    public SwitchEvent(boolean heaterSwitch, boolean fanSwitch) {
        this.heaterSwitch = heaterSwitch;
        this.fanSwitch = fanSwitch;
    }
}
