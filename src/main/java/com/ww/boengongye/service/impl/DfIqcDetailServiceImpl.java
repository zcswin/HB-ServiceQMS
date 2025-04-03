package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfIqcDetail;
import com.ww.boengongye.mapper.DfIqcDetailMapper;
import com.ww.boengongye.service.DfIqcDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

// 实现类
@Service
public class DfIqcDetailServiceImpl extends ServiceImpl<DfIqcDetailMapper, DfIqcDetail>
        implements DfIqcDetailService {

    @Override
    public List<DfIqcDetail> listDetailWithJoin(Wrapper<DfIqcDetail> wrapper) {
        return baseMapper.listDetailWithJoin(wrapper);
    }
}
