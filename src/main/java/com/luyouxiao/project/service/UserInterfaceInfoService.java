package com.luyouxiao.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luyouxiao.project.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
 * @auther : LuYouxiao
 * @date 2024/3/28   -9:14
 * @Description
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo);

    boolean invokeCount(long interfaceInfoId, long userId);

}
