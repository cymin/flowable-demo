package com.tsingyun.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
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

    @Transactional
    public void startProcess() {
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
    }

    /**
     * 分页获取任务
     * @param assignee 任务办理人
     * @param firstResult
     * @param maxResults
     * @return List<Task>
     */
    @Transactional
    public List<Task> getTasksByPage(String assignee, int firstResult, int maxResults) {
        // 分页查询
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).listPage(firstResult, maxResults);
//        final long count = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).count();
        return list;
    }

    /**
     * 获取所有任务
     * @param assignee 任务办理人
     * @return List<Task>
     */
    @Transactional
    public List<Task> getTasks(String assignee) {
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).list();
        return list;
    }
}