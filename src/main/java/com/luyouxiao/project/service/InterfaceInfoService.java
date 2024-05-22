package com.luyouxiao.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luyouxiao.project.common.DeleteRequest;
import com.luyouxiao.project.common.IdRequest;
import com.luyouxiao.project.model.entity.InterfaceInfo;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoAddRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoInvokeRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoQueryRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoUpdateRequest;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther : LuYouxiao
 * @date 2024/3/26   -18:02
 * @Description
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验接口合规
     *
     * @param interfaceInfo
     * @param add 是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 创建接口
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    public Long addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request);

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    public Boolean deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 更新接口
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    public Boolean updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request);

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    public InterfaceInfo getInterfaceInfoById(long id);

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    public List<InterfaceInfo> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    public Page<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request);

    /**
     * 接口发布
     *
     * @param idRequest
     * @param request
     * @return
     */
    public Boolean onlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request);

    /**
     * 接口下线
     *
     * @param idRequest
     * @param request
     * @return
     */
    public Boolean offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request);

    /**
     * 测试调用
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    public Object invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request);
}
