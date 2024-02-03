package com.iot.greenhouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private boolean heaterSwitch;

    @Column
    private boolean fanSwitch;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date timestamp;

    public CommandLog(boolean heaterSwitch, boolean fanSwitch) {
        this.heaterSwitch = heaterSwitch;
        this.fanSwitch = fanSwitch;
    }

    @PrePersist
    private void onCreate() {
        timestamp = new Date(System.currentTimeMillis());
    }
}
