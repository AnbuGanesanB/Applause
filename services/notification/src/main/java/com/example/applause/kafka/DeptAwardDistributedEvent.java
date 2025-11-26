package com.example.applause.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptAwardDistributedEvent {
    private int departmentId;
    private String awardName;
    private String awardDescription;
    private int rewardPoints;
}
