## 默认已提供的rest常用接口：
1. 验证引擎安装正确  
`/management/engine`

2. 查看已经部署的流程定义  
 `/repository/deployments`

3. 验证流程部署正确  
`/repository/process-definitions`

4.  查寻流程实例  
`/runtime/process-instances`

5. 查询任务列表  
`/runtime/tasks`

6. 任务审批  
`/runtime/tasks/{task_id}`

7.多人会签  
http://junlong750.iteye.com/blog/2283195

## 主要的流程图配置说明：
### 1. 单人： 
流程图：

    activiti:assignee="${assignee}"

传参：  

    "variables": [{"name":"assignee","value": "submitUser"}, {"name":"xxxx","value":"xxx"}, ......]


### 2. 多人：
流程图：

    activiti:candidateUsers="${assigneeList}"


传参方式（两种）：

编号 |传参 | 存储类型  
--- | --- | ---
1|"variables": [{ "name" : "assigneeList", "value" : "boss1,boss2"}] | 用逗号隔开的字符串 
2|"variables": [{ "name" : "assigneeList", "value" : ["boss1","boss2"]}] | 存储为二进制
 

## flowable-rest 流程请求步骤：

### 1. 启动流程
    curl -H "Content-Type: application/json" -X POST -d '{"processDefinitionKey":"leave_process", "businessKey":"1", "variables": [{"name":"assignee","value": "submitUser"}]}' http://localhost:8080/runtime/process-instances



### 2. 进行审批

#### new leave 审批通过
    curl -H "Content-Type: application/json" -X POST -d '{"processDefinitionKey":"leave_process", "businessKey":"1", "action" : "complete","variables": [{ "name" : "assigneeList", "value" : ["directLeadership1","directLeadership2"]}]}' http://localhost:8080/runtime/tasks/{taskid}


#### direct leadership 审批通过
    curl -H "Content-Type: application/json" -X POST -d '{"processDefinitionKey":"leave_process", "businessKey":"1", "action" : "complete","variables": [{ "name" : "assigneeList", "value" : ["boss1","boss2"]}]}' http://localhost:8080/runtime/tasks/


#### boss 审批不通过
    curl -H "Content-Type: application/json" -X POST -d '{"processDefinitionKey":"leave_process", "businessKey":"1", "action" : "complete","variables": [{ "name" : "assigneeList", "value" : ["directLeadership1","directLeadership2"]}, {"name" : "flow", "value" : "no"}]}' http://localhost:8080/runtime/tasks/


#### boss 审批通过 (comment需要额外添加了)
    curl -H "Content-Type: application/json" -X POST -d '{"processDefinitionKey":"leave_process", "businessKey":"1", "action" : "complete", "variables": [{"name" : "flow", "value" : "yes"}]}' http://localhost:8080/runtime/tasks/

#### 添加comment
    curl -H "Content-Type: application/json" -X POST -d '{"message" : "boss1 approval, result is yes.","saveProcessInstanceId" : true}' http://localhost:8080/runtime/tasks/5035/comments


