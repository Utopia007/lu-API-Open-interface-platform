package com.luyouxiao.project.model.entity.interfaveInfo;

import lombok.Data;

/**
 * @auther : LuYouxiao
 * @date 2024/3/26   -18:17
 * @Description
 */
@Data
public class InterfaceInfoUpdateRequest {

    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;
    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

}
