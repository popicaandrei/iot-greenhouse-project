package com.iot.greenhouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesiredState {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        @Column
        private Double temperature;

        @Column
        private Double humidity;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = true)
        private Date timestamp;

        @PrePersist
        private void onCreate() {
            timestamp = new Date(System.currentTimeMillis());
        }

}
