package com.kd.controller;

import com.kd.model.WorkflowGroup;
import com.kd.model.WorkflowUser;
import com.kd.service.WorkflowIdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    @Autowired
    private WorkflowIdentityService service;

    @GetMapping("/users")
    public ResponseEntity<?> listUsers(@RequestParam(required = false) String group) {
        List<User> users = service.getUsers(group);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createNewUser(@RequestBody WorkflowUser workflowUser) {
        try {
            User newUser = service.createNewUser(workflowUser);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/groups")
    public ResponseEntity<?> createNewGroup(@RequestBody WorkflowGroup workflowGroup) {
        try {
            Group newGroup = service.createNewGroup(workflowGroup);
            return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<?> addUserToGroup(@PathVariable String groupId, @RequestBody String userId) {
        try {
            service.addMemberToGroup(groupId, userId);
            String message = userId + " has been successfully added to " + groupId;
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
