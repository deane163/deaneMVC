package com.xiaoshu.util;

import com.xiaoshu.annotation.MyController;
import com.xiaoshu.annotation.MyService;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

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
 * @Date : Create in 2018/9/27 20:49
 * <p/>
 * Copyright (C)2013-2018 小树盛凯科技 All rights reserved.
 */
public class ClassHelper {

    private static Set<Class<?>> CLASS_SET;

    static {
        System.out.println("从指定的包路径下面获取所有的类声明 ,并将类放入到 CLASS_SET 容器中");
    }

    /**
     * 获取包下所有的类
     * @return
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> clazzset =  new HashSet<>();
        for(Class<?> clazz : CLASS_SET){
            if(clazz.isAnnotationPresent(MyService.class)){
                clazzset.add(clazz);
            }
        }

        return clazzset;
    }

    /**
     * 获取包下的所有Controller
     * @return
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> clazzset = new HashSet<>();
        for(Class<?> clazz : CLASS_SET){
            if(clazz.isAnnotationPresent(MyController.class)){
                clazzset.add(clazz);
            }
        }
        return  clazzset;
    }

    /**
     * 获取所有的额bean
     * @return
     */
    public static Set<Class<?>> getAllBeans(){
        Set<Class<?>> clazzset = new HashSet<>();
        clazzset.addAll(getServiceClassSet());
        clazzset.addAll(getControllerClassSet());
        return clazzset;
    }

}
