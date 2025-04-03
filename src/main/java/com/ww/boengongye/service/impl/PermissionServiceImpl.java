package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ww.boengongye.entity.Permission;
import com.ww.boengongye.mapper.PermissionMapper;
import com.ww.boengongye.service.PermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
