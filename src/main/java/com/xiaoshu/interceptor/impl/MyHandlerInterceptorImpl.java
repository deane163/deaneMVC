package com.xiaoshu.interceptor.impl;

import com.xiaoshu.annotation.MyInterceptor;
import com.xiaoshu.interceptor.MyHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * @Date : Create in 2018/6/2 16:41
 * <p/>
 * Copyright (C)2013-2018 小树盛凯科技 All rights reserved.
 */
@MyInterceptor
public class MyHandlerInterceptorImpl implements MyHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        System.out.print("===========> 此处可以进行权限认证 <==============");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        System.out.print("===========> Handler 执行后执行方法 <==============");
    }
}
