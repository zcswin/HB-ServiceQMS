package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */

public interface UserService extends IService<User> {
    IPage<User> listJoinPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);

    IPage<User> getUserListBySearch(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);

    List<User> getAllList();

    User getAoiUserByUserCode(@Param(Constants.WRAPPER)Wrapper<User> wrapper);
}
