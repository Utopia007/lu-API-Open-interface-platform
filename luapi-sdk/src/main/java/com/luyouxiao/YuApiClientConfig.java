package com.luyouxiao;

import com.luyouxiao.project.client.YuapiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @auther : LuYouxiao
 * @date 2024/3/27   -12:55
 * @Description
 */
@Configuration
@ConfigurationProperties("yuapi.client")
@Data
@ComponentScan
public class YuApiClientConfig {

    String accessKey;

    String secretKey;

    @Bean
    public YuapiClient yuapiClient(){
        return new YuapiClient(accessKey, secretKey);
    }

}
