package com.tsingyun.controller;

import com.tsingyun.model.TaskRepresentation;
import com.tsingyun.service.MyService;
import com.xiaoleilu.hutool.date.DateUtil;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyRestController {

    @Autowired
    private MyService myService;


    /**
     * 查询待办任务
     *
     * @param assignee 任务办理人
     * @return
     */
    @GetMapping("/tasks")
    public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
        List<Task> tasks = myService.getTasks(assignee);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName(), DateUtil.formatDateTime(task.getCreateTime())));
        }
        return dtos;
    }
}