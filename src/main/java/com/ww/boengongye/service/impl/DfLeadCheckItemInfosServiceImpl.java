package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.mapper.DfLeadCheckItemInfosMapper;
import com.ww.boengongye.service.DfLeadCheckItemInfosService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-29
 */
@Service
public class DfLeadCheckItemInfosServiceImpl extends ServiceImpl<DfLeadCheckItemInfosMapper, DfLeadCheckItemInfos> implements DfLeadCheckItemInfosService {

    @Autowired
    private DfLeadCheckItemInfosMapper dfLeadCheckItemInfosMapper;

//    @Override
//    public List<DfLeadCheckItemInfos> getDetailData(QueryWrapper<DfSizeDetail> wrapper) {
//        return dfLeadCheckItemInfosMapper.getDetailData(wrapper);
//    }
}
