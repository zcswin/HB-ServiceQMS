package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfLeadCheckItemInfosCheck;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.mapper.DfLeadCheckItemInfosCheckMapper;
import com.ww.boengongye.mapper.DfLeadCheckItemInfosMapper;
import com.ww.boengongye.service.DfLeadCheckItemInfosCheckService;
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
 * @since 2025-01-09
 */
@Service
public class DfLeadCheckItemInfosCheckServiceImpl extends ServiceImpl<DfLeadCheckItemInfosCheckMapper, DfLeadCheckItemInfosCheck> implements DfLeadCheckItemInfosCheckService {

    @Autowired
    private DfLeadCheckItemInfosCheckMapper dfLeadCheckItemInfosCheckMapper;

    @Override
    public List<DfLeadCheckItemInfosCheck> getDetailData(QueryWrapper<DfLeadCheckItemInfosCheck> wrapper) {
        return dfLeadCheckItemInfosCheckMapper.getDetailData(wrapper);
    }
}
