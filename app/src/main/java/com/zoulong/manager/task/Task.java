package com.zoulong.manager.task;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * 任务类
 * Created by zl on 2017/6/15/015.
 */
public abstract class Task extends BaseTask implements Runnable,Comparable<Task>{
    private PriorityLevel priorityLevel;//任务优先级
    private TaskListener taskLisnter;
    private TaskReslutListenner resultLisnter;
    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }
    public Task setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
        return  this;
    }

    public Task setTaskLisnter(TaskListener taskLisnter) {
        this.taskLisnter = taskLisnter;
        return this;
    }

    protected void setResultLisnter(TaskReslutListenner resultLisnter) {
        this.resultLisnter = resultLisnter;
    }

    @Override
    public int compareTo(@NonNull Task o) {
        if(getPriorityLevel().ordinal()>o.getPriorityLevel().ordinal()){
            return 1;
        }else{
            return -1;
        }
    }

    public Task callBackOnUI(Activity mActvity){
        this.mActvity = mActvity;
        return this;
    }

    @Override
    public void run() {
        executor();
        resultLisnter.tasksFinish(this);
        if(taskLisnter != null){
            call(new Runnable() {
                @Override
                public void run() {
                    taskLisnter.Onfinish();
                }
            });
        }
    }

    @Override
    public boolean isMine(Task task) {
        return task == task;
    }

    public boolean isFinish(Task task){
        return  isMine(task);
    }
    public abstract void executor();
}
