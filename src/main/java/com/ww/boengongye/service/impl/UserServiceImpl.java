package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ww.boengongye.entity.User;
import com.ww.boengongye.mapper.UserMapper;
import com.ww.boengongye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import javax.xml.ws.Action;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper UserMapper;

    @Override
    public IPage<User> listJoinPage(IPage<User> page, Wrapper<User> wrapper) {
        return UserMapper.listJoinPage(page, wrapper);
    }

    @Override
    public IPage<User> getUserListBySearch(IPage<User> page, Wrapper<User> wrapper) {
        return UserMapper.getUserListBySearch(page,wrapper);
    }

    @Override
    public List<User> getAllList() {
        return UserMapper.getAllList();
    }

    @Override
    public User getAoiUserByUserCode(Wrapper<User> wrapper) {
        return UserMapper.getAoiUserByUserCode(wrapper);
    }
}
