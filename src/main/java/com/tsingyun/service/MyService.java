package com.tsingyun.service;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

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