package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfTzCheckItemInfos;
import com.ww.boengongye.entity.DfTzDetail;
import com.ww.boengongye.mapper.DfTzCheckItemInfosMapper;
import com.ww.boengongye.service.DfTzCheckItemInfosService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * TZ明细表 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
@Service
public class DfTzCheckItemInfosServiceImpl extends ServiceImpl<DfTzCheckItemInfosMapper, DfTzCheckItemInfos> implements DfTzCheckItemInfosService {

    @Autowired
    private DfTzCheckItemInfosMapper dfTzCheckItemInfosMapper;

//    @Override
//    public List<DfTzCheckItemInfos> getDetailData(QueryWrapper<DfTzDetail> wrapper) {
//        return dfTzCheckItemInfosMapper.getDetailData(wrapper);
//    }
}
