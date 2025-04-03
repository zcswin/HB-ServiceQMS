package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfTzCheckItemInfos;
import com.ww.boengongye.entity.DfTzCheckItemInfosCheck;
import com.ww.boengongye.entity.DfTzDetail;
import com.ww.boengongye.mapper.DfTzCheckItemInfosCheckMapper;
import com.ww.boengongye.mapper.DfTzCheckItemInfosMapper;
import com.ww.boengongye.service.DfTzCheckItemInfosCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * TZ测量明细表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
@Service
public class DfTzCheckItemInfosCheckServiceImpl extends ServiceImpl<DfTzCheckItemInfosCheckMapper, DfTzCheckItemInfosCheck> implements DfTzCheckItemInfosCheckService {

    @Autowired
    private DfTzCheckItemInfosCheckMapper dfTzCheckItemInfosCheckMapper;

    @Override
    public List<DfTzCheckItemInfosCheck> getDetailData(QueryWrapper<DfTzCheckItemInfosCheck> wrapper) {
        return dfTzCheckItemInfosCheckMapper.getDetailData(wrapper);
    }
}
