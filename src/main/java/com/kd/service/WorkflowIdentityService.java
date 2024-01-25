package com.kd.service;

import com.kd.model.WorkflowGroup;
import com.kd.model.WorkflowUser;
import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowIdentityService {
    @Autowired
    private IdentityService identityService;

    public List<User> getUsers(String group) {
        List<User> users;
        if(group != null) {
            users = identityService.createUserQuery().memberOfGroup(group).list();
        } else {
            users = identityService.createUserQuery().list();
        }
        return users;
    }

    public User createNewUser(WorkflowUser newWorkflowUser) {
        User user = identityService.newUser(newWorkflowUser.getId());
        user.setFirstName(newWorkflowUser.getFirstName());
        user.setLastName(newWorkflowUser.getLastName());
        user.setEmail(newWorkflowUser.getEmail());
        user.setPassword(newWorkflowUser.getPassword());
        identityService.saveUser(user);
        return user;
    }

    public Group createNewGroup(WorkflowGroup newWorkflowGroup) {
        Group group = identityService.newGroup(newWorkflowGroup.getId());
        group.setName(newWorkflowGroup.getName());
        group.setType(newWorkflowGroup.getType());
        identityService.saveGroup(group);
        return group;
    }

    public void addMemberToGroup(String groupId, String userId) {
        identityService.createMembership(userId, groupId);
    }
}
