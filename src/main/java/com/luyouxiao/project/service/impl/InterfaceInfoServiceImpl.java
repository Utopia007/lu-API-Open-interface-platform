package com.luyouxiao.project.service.impl;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.luyouxiao.project.annotation.AuthCheck;
import com.luyouxiao.project.client.YuapiClient;
import com.luyouxiao.project.common.DeleteRequest;
import com.luyouxiao.project.common.ErrorCode;
import com.luyouxiao.project.common.IdRequest;
import com.luyouxiao.project.constant.CommonConstant;
import com.luyouxiao.project.exception.BusinessException;
import com.luyouxiao.project.mapper.InterfaceInfoMapper;
import com.luyouxiao.project.model.entity.InterfaceInfo;
import com.luyouxiao.project.model.entity.User;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoAddRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoInvokeRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoQueryRequest;
import com.luyouxiao.project.model.entity.interfaveInfo.InterfaceInfoUpdateRequest;
import com.luyouxiao.project.model.enums.InterfaceInfoStatusEnum;
import com.luyouxiao.project.service.InterfaceInfoService;
import com.luyouxiao.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @auther : LuYouxiao
 * @date 2024/3/26   -18:03
 * @Description
 */
@Service
@Slf4j
@DubboService
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private YuapiClient yuapiClient;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestparams = interfaceInfo.getRequestParams();
        String method = interfaceInfo.getMethod();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, description, url, requestparams, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 64) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
        if (StringUtils.isNotBlank(description) && name.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口描述过长");
        }
        if (StringUtils.isNotBlank(url) && name.length() > 128) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口URL过长");
        }
        if (StringUtils.isNotBlank(method) && name.length() > 64) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口类型有误");
        }
    }

    /**
     * 添加接口
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    public Long addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        validInterfaceInfo(interfaceInfo, true);
        boolean result = this.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return interfaceInfo.getId();
    }

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    public Boolean deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        Long deleteRequestId = deleteRequest.getId();
        if (deleteRequest == null || deleteRequestId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 仅本人或管理员可删除
        User loginUser = userService.getLoginUser(request);
        Long userid = getById(deleteRequestId).getUserId();
        if (!userid.equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = removeById(deleteRequestId);
        return b;
    }

    /**
     * 更新接口
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    public Boolean updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        User user = userService.getLoginUser(request);
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = updateById(interfaceInfo);
        return result;
    }

    /**
     * 根据 id 获取当前接口
     *
     * @param id
     * @return
     */
    public InterfaceInfo getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = getById(id);
        return interfaceInfo;
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    public List<InterfaceInfo> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null){
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfo);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        List<InterfaceInfo> interfaceInfoList = list(queryWrapper);
        return interfaceInfoList;
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    public Page<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();

        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfo);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
//        String content = postQuery.getContent();
        // content 需支持模糊搜索
//        postQuery.setContent(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = page(new Page<>(current, size), queryWrapper);
        return interfaceInfoPage;
    }

    /**
     * 接口发布
     *
     * @param idRequest
     * @param request
     * @return
     */
    @Override
    public Boolean onlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断该接口是否可以调用
        com.luyouxiao.project.model.User user = new com.luyouxiao.project.model.User();
//        user.setName("test");
        String username = yuapiClient.getUsernameByPost(user);
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return updateById(interfaceInfo);
    }

    /**
     * 接口下线
     *
     * @param idRequest
     * @param request
     * @return
     */
    @Override
    public Boolean offlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        return updateById(interfaceInfo);
    }

    /**
     * 测试调用
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @Override
    public Object invokeInterfaceInfo(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭/");
        }
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        YuapiClient tempClient = new YuapiClient(accessKey, secretKey);
        Gson gson = new Gson();
        com.luyouxiao.project.model.User user = gson.fromJson(userRequestParams, com.luyouxiao.project.model.User.class);
        String usernameByPost = tempClient.getUsernameByPost(user);
        return usernameByPost;
    }

}
