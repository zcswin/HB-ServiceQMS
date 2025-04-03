package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfTzDetail;
import com.ww.boengongye.entity.DfTzDetailCheck;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfTzDetailCheckMapper;
import com.ww.boengongye.service.DfTzDetailCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * TZ测量 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
@Service
public class DfTzDetailCheckServiceImpl extends ServiceImpl<DfTzDetailCheckMapper, DfTzDetailCheck> implements DfTzDetailCheckService {

    @Autowired
    private DfTzDetailCheckMapper dfTzDetailCheckMapper;

    public List<Rate3> getNgRate(Wrapper<DfTzDetailCheck> wrapper) {
        return dfTzDetailCheckMapper.getNgRate(wrapper);
    }

    @Override
    public List<Rate3> getNgDetailRateTop10(Wrapper<DfTzDetailCheck> wrapper) {
        return dfTzDetailCheckMapper.getNgDetailRateTop10(wrapper);
    }
}
