package com.zoulong.manager.task;

import android.app.Activity;

/**
 * 任务基类
 * Created by zl on 2017/6/15/015.
 */

public abstract class BaseTask {
    private int ID;//当前任务id，由manager分配
    protected Activity mActvity;//当前上下文

    protected int getID() {
        return ID;
    }

    protected void setID(int ID) {
        this.ID = ID;
    }


    protected void call(Runnable runnable){
        if(mActvity != null){
            mActvity.runOnUiThread(runnable);
        }else{
            runnable.run();
        }
    }
    protected abstract boolean isMine(Task task);
    protected abstract boolean isFinish(Task task);
}
