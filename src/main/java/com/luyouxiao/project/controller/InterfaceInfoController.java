package com.luyouxiao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luyouxiao.project.annotation.AuthCheck;
import com.luyouxiao.project.client.YuapiClient;
import com.luyouxiao.project.common.*;
import com.luyouxiao.project.model.entity.InterfaceInfo;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoAddRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoInvokeRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoQueryRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoUpdateRequest;
import com.luyouxiao.project.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther : LuYouxiao
 * @date 2024/3/26   -17:31
 * @Description
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        return ResultUtils.success(interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest, request));
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        Boolean aBoolean = interfaceInfoService.deleteInterfaceInfo(deleteRequest, request);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        Boolean aBoolean = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest, request);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        InterfaceInfo interfaceInfoById = interfaceInfoService.getInterfaceInfoById(id);
        return ResultUtils.success(interfaceInfoById);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        List<InterfaceInfo> interfaceInfos = interfaceInfoService.listInterfaceInfo(interfaceInfoQueryRequest);
        return ResultUtils.success(interfaceInfos);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.listInterfaceInfoByPage(interfaceInfoQueryRequest, request);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 接口发布
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        Boolean aBoolean = interfaceInfoService.onlineInterfaceInfo(idRequest, request);
        return ResultUtils.success(aBoolean);
    }
    /**
     * 下线
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        Boolean aBoolean = interfaceInfoService.offlineInterfaceInfo(idRequest, request);
        return ResultUtils.success(aBoolean);
    }

    /**
     * 测试调用
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        Object o = interfaceInfoService.invokeInterfaceInfo(interfaceInfoInvokeRequest, request);
        return ResultUtils.success(o);
    }


}
