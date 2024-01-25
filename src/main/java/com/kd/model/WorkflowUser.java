package com.kd.model;

import lombok.Data;

@Data
public class WorkflowUser {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
