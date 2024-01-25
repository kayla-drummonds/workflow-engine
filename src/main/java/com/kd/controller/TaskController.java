package com.kd.controller;

import com.kd.model.HolidayRequest;
import com.kd.model.TaskAction;
import com.kd.model.TaskRepresentation;
import com.kd.service.WorkflowService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflow")
public class TaskController {
    @Autowired
    private WorkflowService service;

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String assignee, @RequestParam(required = false) String group) {
        try {
            List<TaskRepresentation> tasks = service.getTasks(assignee, group);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tasks/{id}")
    public ResponseEntity<?> performTaskActions(@PathVariable String id, @RequestBody TaskAction taskAction) {
        try {
            TaskRepresentation action = service.performTaskAction(id, taskAction);
            return new ResponseEntity<>(action, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
