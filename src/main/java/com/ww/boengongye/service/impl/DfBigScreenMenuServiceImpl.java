package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfBigScreenMenu;
import com.ww.boengongye.mapper.DfBigScreenMenuMapper;
import com.ww.boengongye.service.DfBigScreenMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-05-16
 */
@Service
public class DfBigScreenMenuServiceImpl extends ServiceImpl<DfBigScreenMenuMapper, DfBigScreenMenu> implements DfBigScreenMenuService {

    @Autowired
    DfBigScreenMenuMapper DfBigScreenMenuMapper;

    @Override
    public List<DfBigScreenMenu> listByBigScreen(Wrapper<DfBigScreenMenu> wrapper) {
        return DfBigScreenMenuMapper.listByBigScreen(wrapper);
    }
}
