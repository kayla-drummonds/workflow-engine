package com.kd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRepresentation {
    private String id;
    private String name;
    private String assignee;
    private String createTime;
    private String delegationState;
    private boolean approved;
}
