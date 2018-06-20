package com.migu.schedule;


import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.TaskInfo;

import java.util.*;

/*
 *类名和方法不能修改
 */
public class Schedule {

    // 所有节点的信息
    private List<Integer> nodeList = new ArrayList<Integer>();

    // 所有任务的信息
    private List<Integer> taskList = new ArrayList<Integer>();

    // key:服务节点,value:该节点下的任务
    private Map<Integer, List<TaskInfo>> taskListOfNode = new HashMap<Integer, List<TaskInfo>>();

    // 任务,key:任务编号,value:资源消耗率
    private Map<Integer, Integer> taskidAndConsumptionMap = new HashMap<Integer, Integer>();

    // key:消耗资源,value:消耗相同资源量的任务的List
    private Map<Integer, List<Integer>> sameConsumptionTaskMap = new HashMap<Integer, List<Integer>>();

    private int threshold = 0;

    Comparator<TaskInfo> comparator = new Comparator<TaskInfo>() {
        public int compare(TaskInfo o1, TaskInfo o2) {
            return (o1.getTaskId() - o2.getTaskId());
        }
    };

    Comparator<Integer> comparatorByTime = new Comparator<Integer>() {
        public int compare(Integer o1, Integer o2) {
            return (taskidAndConsumptionMap.get(o2) - taskidAndConsumptionMap.get(o1));
        }
    };

    public int init() {
        // 初始化成功，返回E001初始化成功。
        return ReturnCodeKeys.E001;
    }


    public int registerNode(int nodeId) {

        // 如果服务节点编号小于等于0, 返回E004:服务节点编号非法。
        if (nodeId <= 0) {
            return ReturnCodeKeys.E004;
        }

        // 如果服务节点编号已注册, 返回E005:服务节点已注册。
        if (nodeList.contains(nodeId)) {
            return ReturnCodeKeys.E005;
        }

        nodeList.add(nodeId);
        Collections.sort(nodeList);
        return ReturnCodeKeys.E003;


    }

    public int unregisterNode(int nodeId) {

        // 如果服务节点编号小于等于0, 返回E004:服务节点编号非法。
        if (nodeId <= 0) {
            return ReturnCodeKeys.E004;
        }

        // 如果服务节点编号未被注册, 返回E007:服务节点不存在。
        if (!nodeList.contains(nodeId)) {
            return ReturnCodeKeys.E007;
        }
        nodeList.remove(new Integer(nodeId));
        // 注销成功，返回E006:服务节点注销成功。
        return ReturnCodeKeys.E006;

    }


    public int addTask(int taskId, int consumption) {
        // 如果任务编号小于等于0, 返回E009:任务编号非法。
        if (taskId <= 0) {
            return ReturnCodeKeys.E009;
        }
        // 如果相同任务编号任务已经被添加, 返回E010:任务已添加。
        if (taskList.contains(taskId)) {
            return ReturnCodeKeys.E010;
        }
        taskList.add(taskId);
        taskidAndConsumptionMap.put(taskId, consumption);
        Collections.sort(taskList, comparatorByTime);

        // 添加成功，返回E008任务添加成功。
        return ReturnCodeKeys.E008;
    }


    public int deleteTask(int taskId) {
        // 如果任务编号小于等于0, 返回E009:任务编号非法。
        if (taskId <= 0) {
            return ReturnCodeKeys.E009;
        }

        // 如果指定编号的任务未被添加, 返回E012:任务不存在。
        if (!taskList.contains(taskId)) {
            return ReturnCodeKeys.E012;
        }
        taskList.remove(new Integer(taskId));
        taskidAndConsumptionMap.remove(new Integer(taskId));

        // 删除成功，返回E011:任务删除成功。
        return ReturnCodeKeys.E011;
    }


    public int scheduleTask(int threshold) {
        // 没有合适的迁移方案,返回 E014:无合适迁移方案;
        if (taskList.isEmpty()) {
            return ReturnCodeKeys.E014;
        }
        return ReturnCodeKeys.E000;
    }

    public int queryTaskStatus(List<TaskInfo> tasks) {
        // 如果查询结果参数tasks为null，返回E016:参数列表非法
        if (tasks == null){
            return ReturnCodeKeys.E016;
        }
        for (Integer nodeId : taskListOfNode.keySet()) {
            tasks.addAll(taskListOfNode.get(nodeId));
        }
        Collections.sort(tasks, comparator);
        System.out.println(tasks);
        // 如果查询成功, 返回E015: 查询任务状态成功;查询结果从参数Tasks返回。
        return ReturnCodeKeys.E015;
    }

}
