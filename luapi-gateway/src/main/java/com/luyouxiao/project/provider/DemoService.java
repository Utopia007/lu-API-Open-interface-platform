package com.luyouxiao.project.provider;

/**
 * @auther : LuYouxiao
 * @date 2024/3/16   -23:07
 * @Description
 */
import java.util.concurrent.CompletableFuture;
public interface DemoService {
    String sayHello(String name);
    String sayHello2(String name);
    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }
}