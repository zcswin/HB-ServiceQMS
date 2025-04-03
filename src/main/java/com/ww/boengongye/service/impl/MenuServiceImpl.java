package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ww.boengongye.entity.Menu;
import com.ww.boengongye.mapper.MenuMapper;
import com.ww.boengongye.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-01-25
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
