package com.luyouxiao.project.model.entity.interfaveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther : LuYouxiao
 * @date 2024/3/27   -16:42
 * @Description
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;

}
