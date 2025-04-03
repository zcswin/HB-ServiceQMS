package com.ww.boengongye.mapper;

//import com.baomidou.dynamic.datasource.annotation.DS;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

}
