package com.xiaoshu.servlet;

import com.xiaoshu.annotation.MyController;
import com.xiaoshu.annotation.MyInterceptor;
import com.xiaoshu.annotation.MyRequestMapping;
import com.xiaoshu.interceptor.MyHandlerInterceptor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

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
 * @Date : Create in 2018/5/31 15:10
 * <p/>
 * Copyright (C)2013-2018 小树盛凯科技 All rights reserved.
 */
public class MyDispatcherServlet extends HttpServlet{

    private Properties properties = new Properties();
    private List<String> classNames = new ArrayList<>();
    // 拦截器的列表名称
    private List<String> interceptorClassName = new ArrayList<>();

    private Map<String,Object> iocMap = new HashMap<>();
    private List<MyHandlerInterceptor> interceptors = new ArrayList<>();


    private Map<String, Method> handlerMapping = new  HashMap<>();

    private Map<String, Object> controllerMap  =new HashMap<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.print("start initial the Servlet");

        // 1、 加载配置文件信息  application.properties
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 2、 获得所有的类名，（IOC和Interceptor）
        doScane(properties.getProperty("scanIocPackage"),classNames);

        doScane(properties.getProperty("scanInteceptorPackage"),interceptorClassName);
        System.out.println("start load class Name");
        // 3、 生成类的对象，并保存到自己创建的IOC容器中
        doInstance(classNames, MyController.class,iocMap);

        // 4、 创建 URL和Method的映射关系，同时创建URL和 Controller控制类的映射关系
        initHandlerMapping();

        // 5、 初始化Interceor拦截器的调用链
        initInterceptorChain(interceptorClassName);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {

        doPost(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        doDispatcher(req,resp);
    }


    private void doDispatcher(HttpServletRequest request, HttpServletResponse response){
        System.out.println("start do doDispatcher server ...");
        if(handlerMapping == null){
            try {
                response.getWriter().write("URL Mapping is not exists");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        //拼接url并把多个/替换成一个
        url = url.replace(contextPath,"").replaceAll("/+","/");
        if(!this.handlerMapping.containsKey(url)){
            try {
                response.getWriter().write("404 NOT FOUND!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        // 通过Method 进行反射执行方法
        Method method = this.handlerMapping.get(url);
        //获取方法的参数列表
        Class<?>[] paramterTypes = method.getParameterTypes();
        //获取请求的参数
        Map<String, String[]> paramterMap = request.getParameterMap();
        //保存参数值
        Object[] paramValues = new Object[paramterTypes.length];

        //方法的参数列表
        for (int i = 0; i<paramterTypes.length; i++) {
            //根据参数名称，做某些处理
            String requestParam = paramterTypes[i].getSimpleName();
            if (requestParam.equals("HttpServletRequest")) {
                //参数类型已明确，这边强转类型
                paramValues[i] = request;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")) {
                paramValues[i] = response;
                continue;
            }
        }
        // 执行拦截器的方法进行处理
        if(interceptors != null && interceptors.size() > 0){
            for (MyHandlerInterceptor interceptor : interceptors){
                //执行拦截器操作
                if(!interceptor.preHandle(request,response)){
                    return;
                }
            }
        }
        // 执行实际的Handler操作
        try {
            method.invoke(this.controllerMap.get(url),paramValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 执行拦截器的postHandle操作
        if(interceptors != null && interceptors.size() > 0){
            for(MyHandlerInterceptor interceptor : interceptors){
                interceptor.postHandle(request,response);
            }
        }

    }

    // 1、加载配置文件，并将配置文件数据写入 Properties
    private void doLoadConfig(String location){
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(location);
        try {
            properties.load(stream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != stream){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 2、扫描IOC包，
    private  void doScane(String packageName, List<String> list){
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for(File file :dir.listFiles()){
            if(file.isDirectory()){
                // 如果是文件夹，则递归读取包
                doScane(packageName +"." + file.getName(),list);
            }else{

                String className = packageName + "." + file.getName().replaceAll(".class","");
                list.add(className);
            }
        }

    }

    // 3、实例化
    private void doInstance(List<String> classPaths, Class annotationClass,Map map) {
        if(classPaths.isEmpty()){
            // 如果文件夹下面没有类，则直接返回
            return;
        }
        for(String className : classPaths){
            try {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(annotationClass)){
                    map.put(toLowerFirstWord(clazz.getSimpleName()),clazz.newInstance());
                }else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // 4、 初始化 Mapping到Handler的映射关系
    private void initHandlerMapping(){
        if(iocMap == null || iocMap.size() < 1){
            return;
        }
        for(Map.Entry<String,Object> entry : iocMap.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            if(!clazz.isAnnotationPresent(MyController.class)){
                continue;
            }

            //拼url时,是controller头的url拼上方法上的url
            String baseUrl = "";
            if(clazz.isAnnotationPresent(MyRequestMapping.class)){
                MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
                baseUrl = annotation.value();
            }
            Method[] methods = clazz.getMethods();
            for(Method method : methods){
                if(!method.isAnnotationPresent(MyRequestMapping.class)){
                    continue;
                }
                MyRequestMapping methodAnnotation = method.getAnnotation(MyRequestMapping.class);
                String url = methodAnnotation.value();
                url =(baseUrl+"/"+url).replaceAll("/+", "/");
                handlerMapping.put(url, method);
                controllerMap.put(url,entry.getValue());
                System.out.println("========>" + url +"" + method);
            }
        }

    }

    // 5、 初始化 Interceptor 拦截链
    private void initInterceptorChain(List<String> classPaths){
        if(classPaths.isEmpty()){
            return;
        }
        for(String className: classPaths){
            try {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(MyInterceptor.class)){
                    interceptors.add((MyHandlerInterceptor)clazz.newInstance());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 把字符串的首字母小写
     * @param name
     * @return
     */
    private String toLowerFirstWord(String name){
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

}
