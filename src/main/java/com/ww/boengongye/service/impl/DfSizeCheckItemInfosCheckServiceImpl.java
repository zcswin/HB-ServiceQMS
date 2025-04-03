package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.DfSizeCheckItemInfosCheck;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.DfSizeDetailCheck;
import com.ww.boengongye.mapper.DfSizeCheckItemInfosCheckMapper;
import com.ww.boengongye.service.DfSizeCheckItemInfosCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2025-01-12
 */
@Service
public class DfSizeCheckItemInfosCheckServiceImpl extends ServiceImpl<DfSizeCheckItemInfosCheckMapper, DfSizeCheckItemInfosCheck> implements DfSizeCheckItemInfosCheckService {

    @Autowired
    private DfSizeCheckItemInfosCheckMapper dfSizeCheckItemInfosCheckMapper;

    public List<Map<String,Object>> getDetailData(Wrapper<DfSizeDetailCheck> wrapper) {
        return dfSizeCheckItemInfosCheckMapper.getDetailData(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfosCheck> getSizeCheckItemInfosList(Wrapper<DfSizeDetailCheck> wrapper) {
        return dfSizeCheckItemInfosCheckMapper.getSizeCheckItemInfosList(wrapper);
    }
}
