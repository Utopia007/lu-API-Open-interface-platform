//package com.yupi.project.provider;
//
//import org.apache.dubbo.config.annotation.DubboService;
//import org.apache.dubbo.rpc.RpcContext;
//
///**
// * @auther : LuYouxiao
// * @date 2024/3/16   -23:07
// * @Description
// */
//@DubboService
//public class DemoServiceImpl implements DemoService{
//
//    @Override
//    public String sayHello(String name) {
//        System.out.println("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
//        return "Hello " + name;
//    }
//    @Override
//    public String sayHello2(String name) {
//        return "luyouxiao";
//    }
//}
