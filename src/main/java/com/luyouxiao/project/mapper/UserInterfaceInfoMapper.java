package com.luyouxiao.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luyouxiao.project.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 乔冠豪
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-02-28 16:29:15
* @Entity generator.domain.UserInterfaceInfo
*/
@Repository
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




