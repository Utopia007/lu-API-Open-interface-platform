package com.luyouxiao.project.model.vo;

import com.luyouxiao.project.model.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

/**
 *  接口信息封装视图
 * @auther : LuYouxiao
 * @date 2024/2/17   -15:19
 * @Description
 */
@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {

    /**
     * 调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 7060581931090558401L;


}
