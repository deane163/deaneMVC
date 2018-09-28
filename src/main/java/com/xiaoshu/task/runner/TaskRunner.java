package com.xiaoshu.task.runner;

import com.xiaoshu.task.Task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * code is far away from bug with the animal protecting
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 *
 * @Description :
 * ---------------------------------
 * @Author : ubt.administrator
 * @Date : Create in 2018/9/28 14:28
 * <p/>
 * Copyright (C)2013-2018 小树盛凯科技 All rights reserved.
 */
public abstract class TaskRunner implements Runnable{

    private ExecutorService executorService;

    /**
     * 默认的执行线程数
     */
    private static final int defaultPoolSize = 1;

    /**
     * 单例类
     */
    public static TaskRunner taskRunner;

    protected  Queue<Task> taskQueue;

    public TaskRunner(int poolSize) {
        super();
        executorService = Executors.newFixedThreadPool(poolSize);
    }

    public void addTask(Task task){
        taskQueue.add(task);
    }

    public static TaskRunner getInstance(Class<? extends TaskRunner> clazz, int poolSize){
        try {
            Constructor<? extends TaskRunner> constructor =  clazz.getConstructor(int.class);
            taskRunner = constructor.newInstance(poolSize);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 返回 TaskRunner的实体类
        return taskRunner;
    }


    public void execute(){
        Task task = taskQueue.poll();
        if(null != task)
        {
            if(task.canExecute()){
                executorService.execute(task);
            }else{
                taskQueue.add(task);
            }
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
