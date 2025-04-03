package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.LineBody;
import com.ww.boengongye.mapper.LineBodyMapper;
import com.ww.boengongye.service.LineBodyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-08
 */
@Service
public class LineBodyServiceImpl extends ServiceImpl<LineBodyMapper, LineBody> implements LineBodyService {

    @Autowired
    LineBodyMapper LineBodyMapper;

    @Override
    public IPage<LineBody> listJoinIds(IPage<LineBody> page, Wrapper<LineBody> wrapper) {
        return LineBodyMapper.listJoinIds(page, wrapper);
    }
}
