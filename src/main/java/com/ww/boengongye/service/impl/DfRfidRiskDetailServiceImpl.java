package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfRfidRiskDetail;
import com.ww.boengongye.mapper.DfRfidRiskDetailMapper;
import com.ww.boengongye.service.DfRfidRiskDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 风险品信息表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
@Service
public class DfRfidRiskDetailServiceImpl extends ServiceImpl<DfRfidRiskDetailMapper, DfRfidRiskDetail> implements DfRfidRiskDetailService {

    @Autowired
    DfRfidRiskDetailMapper dfRfidRiskDetailMapper;

    @Override
    public List<DfRfidRiskDetail> listJoinAppearance(String time,Wrapper<DfRfidRiskDetail> wrapper) {
        return dfRfidRiskDetailMapper.listJoinAppearance(time,wrapper);
    }

    @Override
    public List<DfRfidRiskDetail> listJoinSize(String time,Wrapper<DfRfidRiskDetail> wrapper) {
        return dfRfidRiskDetailMapper.listJoinSize(time,wrapper);
    }
}
