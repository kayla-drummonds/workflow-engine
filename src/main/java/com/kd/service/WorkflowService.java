package com.kd.service;

import com.kd.model.HolidayRequest;
import com.kd.model.TaskAction;
import com.kd.model.TaskRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class WorkflowService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    public ProcessInstance startProcess(String key) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
        return processInstance;
    }

    public List<TaskRepresentation> getTasks(String assignee, String group) {
        List<TaskRepresentation> jsonList = new ArrayList<>();
        List<Task> tasks;
        if(assignee != null) {
            tasks = taskService.createTaskQuery().taskCandidateUser(assignee).list();
        } else if (group != null){
            tasks = taskService.createTaskQuery().taskCandidateGroup(group).list();
        } else {
            tasks = taskService.createTaskQuery().list();
        }

        for(Task task : tasks) {
            TaskRepresentation json = convertToJson(task);
            jsonList.add(json);
        }

        return jsonList;
    }

    public TaskRepresentation performTaskAction(String id, TaskAction taskAction) {
        String action = taskAction.getAction();
        Task task = taskService.createTaskQuery().taskId(id).list().get(0);
        switch(action) {
            case "complete":
                taskService.complete(id);
                break;
            case "claim":
                taskService.claim(id,taskAction.getAssignee());
                break;
            case "delegate":
                taskService.delegateTask(id,taskAction.getAssignee());
                break;
            case "resolve":
                taskService.resolveTask(id);
                break;
            default:
                break;
        }
        Task updatedTask = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        TaskRepresentation json = convertToJson(updatedTask);
        return json;
    }

    public TaskRepresentation convertToJson(Task task) {
        TaskRepresentation json = new TaskRepresentation();
        json.setId(task.getId());
        json.setName(task.getName());
        json.setAssignee(task.getAssignee());
        json.setCreateTime(task.getCreateTime().toString());
        if(task.getDelegationState() != null) {
            json.setDelegationState(task.getDelegationState().toString());
        }
        if(task.getProcessVariables().get("approved") != null) {
            boolean approved = (boolean) task.getProcessVariables().get("approved");
            json.setApproved(approved);
        }
        return json;
    }
}
