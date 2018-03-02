package com.tsingyun.controller;

import com.tsingyun.model.TaskRepresentation;
import com.tsingyun.service.MyService;
import com.xiaoleilu.hutool.date.DateUtil;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRestController.class);

    private final static String PROCESS_DEFINITION_KEY  = "leave_process";

    @Autowired
    private MyService myService;


    /**
     * 根据assignee查询待办任务
     *
     * @param assignee 任务办理人
     * @return
     */
    @GetMapping("/tasks-by-assignee/{assignee}")
    public List<TaskRepresentation> getTasksByAssignee(@PathVariable String assignee) {
        List<Task> tasks = myService.getTasksByAssignee(assignee);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName(), DateUtil.formatDateTime(task.getCreateTime())));
        }
        return dtos;
    }

    /**
     * 根据processDefinitionKey查询待办任务
     *
     * @param processDefinitionKey 流程定义key
     * @return
     */
    @GetMapping("/tasks-by-process-definition-key")
    public List<TaskRepresentation> getTasksByProcessDefinitionKey(String processDefinitionKey) {
        if (StringUtils.isEmpty(processDefinitionKey)) {
            processDefinitionKey = PROCESS_DEFINITION_KEY;
        }
        List<Task> tasks = myService.getTasksByProcessDefinitionKey(processDefinitionKey);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName(), DateUtil.formatDateTime(task.getCreateTime())));
        }
        return dtos;
    }

    /**
     * 获取已完成的历史任务
     *
     * @return
     */
    @GetMapping("/historic/finished-tasks")
    public List<TaskRepresentation> getHistoricTasks() {
        List<HistoricTaskInstance> tasks = myService.getFinishedHistoricTasks();
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (HistoricTaskInstance task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName(), DateUtil.formatDateTime(task.getCreateTime())));
        }
        return dtos;
    }

    /**
     * 启动流程
     * @param processDefinitionKey
     * @param businessKey 业务key
     * @param assigneeList 任务办理人
     * @return
     */
    @GetMapping("/task/start")
    public String startProcessByProcessDefinitionKey(String processDefinitionKey, String businessKey, String assigneeList) {
        if (StringUtils.isEmpty(processDefinitionKey)) {
            processDefinitionKey = PROCESS_DEFINITION_KEY;
        }
        if (StringUtils.isEmpty(businessKey)) {
            businessKey = "businessKey1";
        }
         if (StringUtils.isEmpty(assigneeList)) {
             assigneeList = "Initiator";
        }
        String processInstanceId = myService.startProcessByProcessDefinitionKey(processDefinitionKey, businessKey, assigneeList);
        return processInstanceId;
    }
}