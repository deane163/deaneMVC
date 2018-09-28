package com.xiaoshu.task.impl;

import com.xiaoshu.task.Task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
 * @Date : Create in 2018/9/28 15:08
 * <p/>
 * Copyright (C)2013-2018 小树盛凯科技 All rights reserved.
 */
public class ReflectTask implements Task {

    private Object target;

    private String methodName;

    /**
     * 执行方法参数
     */
    private Object[] args;

    public ReflectTask(Object target, String methodName, Object[] args) {
        this.target = target;
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public boolean canExecute() {
        // 判断执行条件
        return true;
    }

    @Override
    public void run() {
        Class<?>[] paramtersTypes = null;
        if(args != null){
            paramtersTypes = new Class<?>[args.length];
            for(int i=0; i< args.length; i++){
                paramtersTypes[i] = args[i].getClass();
            }
        }
        Class targetClazz = target.getClass();
        try {
            Method method = targetClazz.getMethod(methodName, paramtersTypes);
            method.invoke(target,args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
