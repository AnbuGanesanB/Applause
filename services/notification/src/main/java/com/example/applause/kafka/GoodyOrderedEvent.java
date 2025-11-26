package com.example.applause.kafka;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodyOrderedEvent {

    private String empUuid;
    private String goodyName;
    private int qty;
    private LocalDateTime dateTime;
}
