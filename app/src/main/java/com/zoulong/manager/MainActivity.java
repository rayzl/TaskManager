package com.zoulong.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zoulong.manager.task.TaskListener;
import com.zoulong.manager.task.TaskReslutListenner;
import com.zoulong.manager.task.PriorityLevel;
import com.zoulong.manager.task.Task;
import com.zoulong.manager.task.TaskGroup;
import com.zoulong.manager.task.TaskManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskManager.getInstance().init(3,4,3000, TimeUnit.MILLISECONDS,this);
        Test();
        findViewById(R.id.test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.getInstance().setCorePoolSize(4);
            }
        });
    }

    private int test=0;
    private void Test() {
        System.out.println("测试");
        TaskGroup taskGroup = TaskGroup.creatTaskGroup()
                                        .callBackOnUI(this);
              for(int i=0;i<10;i++){
            PriorityLevel levle = PriorityLevel.FOURTH;
            if(i%4==0){
                levle = PriorityLevel.FRIST;
            }else if(i%4 == 1){
                levle = PriorityLevel.SECOND;
            }else if(i%4 == 2){
                levle = PriorityLevel.THIRD;
            }
            taskGroup.addTakToGroup(new Task() {
                @Override
                public void executor() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.setPriorityLevel(levle)
            .setTaskLisnter(new TaskListener() {
                @Override
                public void Onfinish() {
                    Toast.makeText(MainActivity.this,"我是在主线程回调的哦",Toast.LENGTH_SHORT).show();
                }
            }).callBackOnUI(this));
        }
        taskGroup.setListener(new TaskListener() {
            @Override
            public void Onfinish() {
                System.out.println("---------------------任务组结束----------------------");
                Toast.makeText(MainActivity.this,"我是在主线程回调的哦",Toast.LENGTH_SHORT).show();
            }
        });
        TaskManager.getInstance().executorTaskGroups(taskGroup);
        for(int i=0;i<20;i++){
            PriorityLevel levle = PriorityLevel.FOURTH;
            if(i%4==0){
                levle = PriorityLevel.FRIST;
            }else if(i%4 == 1){
                levle = PriorityLevel.SECOND;
            }else if(i%4 == 2){
                levle = PriorityLevel.THIRD;
            }
            TaskManager.getInstance().executor(new Task() {
                @Override
                public void executor() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.setPriorityLevel(levle));
        }
    }
}
