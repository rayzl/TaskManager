package com.zoulong.manager.task;

import android.app.Activity;

import java.util.ArrayList;

/**
 * 任务组类
 * Created by zl on 2017/6/15/015.
 */
public class TaskGroup extends BaseTask{
    private ArrayList<Task> tasks;
    private int groupID;
    private TaskListener taskListener;

    public static TaskGroup creatTaskGroup(){
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setGroupID(TaskManager.getInstance().obtainTaskId());
        taskGroup.tasks = new ArrayList<Task>();
        return taskGroup;
    }

    public void setListener(TaskListener listener) {
        this.taskListener = listener;
    }

    public int getGroupID() {
        return groupID;
    }

    private void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void addTakToGroup(Task task){
        task.setID(TaskManager.getInstance().obtainTaskId());
        tasks.add(task);
    }

    public TaskGroup callBackOnUI(Activity mActivity){
        this.mActvity = mActivity;
        return this;
    }

    protected ArrayList<Task> getTasks() {
        return tasks;
    }

    protected boolean isMine(Task task){
        return tasks.remove(task);
    }

    protected boolean isFinish(Task task){
        if(tasks.size() == 0){
            call(new Runnable() {
                @Override
                public void run() {
                    taskListener.Onfinish();
                }
            });
            return true;
        }
        return false;
    }
}
