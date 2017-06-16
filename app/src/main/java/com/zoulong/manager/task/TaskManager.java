package com.zoulong.manager.task;

import android.app.Activity;
import android.util.Log;

import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务管理类
 * 使用线程池
 * 所有任务都不必另开线程
 * 支持任务组和单个任务
 * Created by zl on 2017/6/15/015.
 */
public class TaskManager implements TaskReslutListenner{
    private String Tag = getClass().getSimpleName();
    private static TaskManager taskManager;
    private ThreadPoolExecutor threadPool;
    private Activity mActivity;
    private PriorityBlockingQueue<Runnable> blockingQueue;
    private int taskId=0;
    private Vector<BaseTask> tasks;
    public static TaskManager getInstance(){
        if(taskManager == null){
            taskManager = new TaskManager();
        }
        return  taskManager;
    }

    public void init(int corePoolSize,
                     int maximumPoolSize,
                     long keepAliveTime,
                     TimeUnit unit,Activity mActivity){
        this.mActivity = mActivity;//上下文
        tasks = new Vector<BaseTask>();
        blockingQueue = new PriorityBlockingQueue<Runnable>();//新建一个有优先级的队列
        threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,blockingQueue);//配置线程池
        threadPool.allowCoreThreadTimeOut(true);//允许核心线程被回收
    }

    /**
     * 扩展核心线程数量
     * @param corePoolSize
     */
    public void setCorePoolSize(int corePoolSize){
        if(corePoolSize>threadPool.getMaximumPoolSize()){
            Log.e(Tag,"corepoolsize greater than maximumpoolsize");
            return;
        }else if(corePoolSize == threadPool.getCorePoolSize()){
            return;
        }
        threadPool.setCorePoolSize(corePoolSize);
    }

    /**
     * 添加任务
     * @param task 添加任务
     */
    public void executor(Task task){
        if(task == null){
            Log.e(Tag,"task is null");
            return;
        }
        task.setID(obtainTaskId());
        task.setResultLisnter(this);
        threadPool.execute(task);
        tasks.add(task);
    }

    /**
     * 添加任务组
     * @param taskGroup 任务组
     */
    public void executorTaskGroups(TaskGroup taskGroup){
        if(taskGroup == null){
            Log.e(Tag,"taskgroup is null");
            return;
        }
        for(Task task:taskGroup.getTasks()){
            task.setResultLisnter(this);
            threadPool.execute(task);
        }
        tasks.add(taskGroup);
    }

    /**
     * 任务id
     * @return
     */
    public synchronized int obtainTaskId(){
        taskId++;
        return taskId;
    }

    /**
     * 线程的极限个数
     * @return 能支持的线程的最大个数
     */
    public int getMaxThreadCount(){
        return Runtime.getRuntime().availableProcessors() * 3 + 2;
    }

    @Override
    public void tasksFinish(Task finishTask) {
        for(BaseTask baseTask:tasks){
            if(baseTask.isMine(finishTask)){
                if(baseTask instanceof TaskGroup){
                    if(baseTask.isFinish(finishTask)){
                        tasks.remove(baseTask);
                        break;
                    }
                }else{
                    tasks.remove(baseTask);
                    break;
                }
            }
        }
    }
}
