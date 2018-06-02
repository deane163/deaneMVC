package com.xiaoshu.controller;

import com.xiaoshu.annotation.MyController;
import com.xiaoshu.annotation.MyRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
 * @Date : Create in 2018/6/2 15:11
 * <p/>
 * Copyright (C)2013-2018 小树盛凯科技 All rights reserved.
 */
@MyController
@MyRequestMapping(value = "/test")
public class TestController {

    @MyRequestMapping(value = "/doTest1")
    public void doTest1(HttpServletRequest request, HttpServletResponse response){
        String ip = request.getRemoteAddr();
        System.out.println("do Test1：" + ip);
        try {
            response.getWriter().write("do Test1：" + ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MyRequestMapping(value = "/doTest2")
    public void doTest2(HttpServletRequest request, HttpServletResponse response){
        String host = request.getRemoteHost();
        System.out.println("do Test2：" + host);
        try {
            response.getWriter().write("do Test2：" + host);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
