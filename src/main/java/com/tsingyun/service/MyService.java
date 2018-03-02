package com.tsingyun.service;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 通过processDefinitionKey启动流程
     * @param processDefinitionKey 流程定义key
     * @return 流程实例ID
     */
    @Transactional
    public String startProcessByProcessDefinitionKey(String processDefinitionKey, String businessKey, String assigneeList) {
        // 设置流程的启动人
        Authentication.setAuthenticatedUserId("Initiator");

        //设置流程变量，设置下一个任务的办理组
        final Map<String, Object> map = new HashMap<>();
        map.put("assignee", assigneeList);

        // 启动流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, map);

        // 为了防止多线程时内存泄露，需要设置为null
        Authentication.setAuthenticatedUserId(null);

        return processInstance.getId();
    }

    /**
     * 分页获取某个人的待办任务
     * @param assignee 任务办理人
     * @param firstResult
     * @param maxResults
     * @return List<Task>
     */
    @Transactional
    public List<Task> getTasksByAssignee(String assignee, int firstResult, int maxResults) {
        // 分页查询
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).orderByTaskCreateTime().desc().listPage(firstResult, maxResults);
//        final long count = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).count();
        return list;
    }

    /**
     * 获取某个人的所有待办任务
     * @param assignee 任务办理人
     * @return List<Task>
     */
    @Transactional
    public List<Task> getTasksByAssignee(String assignee) {
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).list();
        return list;
    }

    /**
     * 获取所有待办任务
     * @param processDefinitionKey 流程定义key
     * @return List<Task>
     */
    @Transactional
    public List<Task> getTasksByProcessDefinitionKey(String processDefinitionKey) {
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).list();
        return list;
    }

    /**
     * 获取已完成的历史任务
     * @return List<HistoricTaskInstance>
     */
    @Transactional
    public List<HistoricTaskInstance> getFinishedHistoricTasks() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().orderByHistoricTaskInstanceEndTime().desc().finished().list();
        return list;
    }
}