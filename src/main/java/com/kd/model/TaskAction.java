package com.kd.model;

import lombok.Data;

@Data
public class TaskAction {
    private String action;
    private String assignee;
    private boolean approved;
}
