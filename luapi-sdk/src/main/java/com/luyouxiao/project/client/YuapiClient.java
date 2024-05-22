package com.luyouxiao.project.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.luyouxiao.project.model.User;
import com.luyouxiao.project.utils.SignUtils;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther : LuYouxiao
 * @date 2024/3/27   -11:41
 * @Description
 */
public class YuapiClient {

    private String accessKey;

    private String secretKey;

    public YuapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getUsernameByGet(String name){
        //GET请求 8020是网关端口
        String content = HttpUtil.get("http://localhost:8020/api?name=" + name);
        return content;
    }

    public String getUsernameByPost(String name){
        //POST请求
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post("http://localhost:8020/api", paramMap);
    }


    public String getUsernameByPost(User user){
        String jsonStr = JSONUtil.toJsonStr(user);
        return HttpRequest.post("http://localhost:8020/api/name/user")
                .addHeaders(getHeaderMap(jsonStr))
                .body(jsonStr)
                .execute().body();
    }

    private Map<String, String> getHeaderMap(String body) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("accessKey", accessKey);
        paramMap.put("body", body);
        paramMap.put("nonce", RandomUtil.randomNumbers(4));
        paramMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        paramMap.put("sign", SignUtils.genSign(body, secretKey));
        return paramMap;
    }

}
