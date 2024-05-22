package com.luyouxiao.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luyouxiao.project.model.entity.InterfaceInfo;
import org.springframework.stereotype.Repository;

/**
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
* @createDate 2024-02-17 11:00:19
* @Entity generator.domain.InterfaceInfo
*/
@Repository
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    InterfaceInfo selectInterfaceInfoById(Long id);

}




