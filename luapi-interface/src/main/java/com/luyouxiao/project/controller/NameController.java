package com.luyouxiao.project.controller;

import com.luyouxiao.project.client.YuapiClient;
import com.luyouxiao.project.model.User;
import com.luyouxiao.project.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @auther : LuYouxiao
 * @date 2024/3/26   -21:52
 * @Description
 */
@RestController
@RequestMapping("/name")
public class NameController {

    String caccessKey = "1f211489e0afa33bd33d1cf2958ccb89";
    String csecretKey = "10cfbfe19d338711167114c2dbcae0fa";

    @Resource
    private YuapiClient yuapiClient;

    @GetMapping
    public String getNameByGet(String name){
        return "GET 转发的名字是" + name;
    }

    @PostMapping
    public String getNameByPost(@RequestParam String name){
        return "POST 转发的名字是" + name;
    }

    @PostMapping("/user")
    public String getNameByPost(@RequestBody User user){
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String body = request.getHeader("body");
//        String sign = request.getHeader("sign");
//        if (!accessKey.equals(caccessKey)){
//            throw new RuntimeException("access不对ppp");
//        }
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("nonce不对ppp");
//        }
//        String sign1 = SignUtils.genSign(body, csecretKey);
//        if(!sign.equals(sign1)){
//            throw new RuntimeException("sign不对aaaaaaaa");
//        }
        //todo 调用次数+1

        String result = "POST USER 转发的名字是" + user.getName();
        return result;
    }


}
