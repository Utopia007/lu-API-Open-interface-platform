package com.luyouxiao.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luyouxiao.project.common.ErrorCode;
import com.luyouxiao.project.exception.BusinessException;
import com.luyouxiao.project.mapper.UserInterfaceInfoMapper;
import com.luyouxiao.project.model.entity.UserInterfaceInfo;
import com.luyouxiao.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @auther : LuYouxiao
 * @date 2024/3/28   -9:15
 * @Description
 */
@Service
@DubboService
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {


    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if (interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (leftNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用接口次数参数错误");
        }

    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 添加分布式锁保证事务一致性
        UpdateWrapper<UserInterfaceInfo> userInterfaceInfoUpdateWrapper = new UpdateWrapper<>();
        userInterfaceInfoUpdateWrapper.eq("interfaceInfoId", interfaceInfoId);
        userInterfaceInfoUpdateWrapper.eq("userId", userId);
        userInterfaceInfoUpdateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        boolean update = update(userInterfaceInfoUpdateWrapper);
        return update;
    }


}
