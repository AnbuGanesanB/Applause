package com.example.applause.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndlAwardDistributedEvent {
    private List<String> empUuids;
    private String awardName;
    private String awardDescription;
    private int rewardPoints;
}
