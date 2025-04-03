package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetail;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import com.ww.boengongye.mapper.DfQmsIpqcWaigDetailMapper;
import com.ww.boengongye.service.DfQmsIpqcWaigDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Service
public class DfQmsIpqcWaigDetailServiceImpl extends ServiceImpl<DfQmsIpqcWaigDetailMapper, DfQmsIpqcWaigDetail> implements DfQmsIpqcWaigDetailService {

    @Autowired
    DfQmsIpqcWaigDetailMapper dfQmsIpqcWaigDetailMapper;

    @Override
    public List<DfQmsIpqcWaigDetail> listByJoin(Wrapper<DfQmsIpqcWaigDetail> wrapper) {
        return dfQmsIpqcWaigDetailMapper.listByJoin(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigDetail> listBySmAreaCount(Wrapper<DfQmsIpqcWaigDetail> wrapper) {
        return dfQmsIpqcWaigDetailMapper.listBySmAreaCount(wrapper);
    }

    @Override
    public DfQmsIpqcWaigDetail getSumAffectCount(Wrapper<DfQmsIpqcWaigDetail> wrapper) {
        return dfQmsIpqcWaigDetailMapper.getSumAffectCount(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> fullApperanceNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfQmsIpqcWaigDetailMapper.fullApperanceNGTop5(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate3> getFpy(Wrapper<Rate3> wrapper, Wrapper<Rate3> wrapper2) {
        return dfQmsIpqcWaigDetailMapper.getFpy(wrapper,wrapper2);
    }

    @Override
    public List<Rate3> getAfterFpy(Wrapper<Rate3> wrapper, Wrapper<Rate3> wrapper2) {
        return dfQmsIpqcWaigDetailMapper.getAfterFpy(wrapper,wrapper2);
    }

    @Override
    public List<Rate4> getAlarmMessage(Double alarmValue) {
        return dfQmsIpqcWaigDetailMapper.getAlarmMessage(alarmValue);
    }

    @Override
    public List<Rate4> getWarnMessage(Double alarmValue,Double warnValue) {
        return dfQmsIpqcWaigDetailMapper.getWarnMessage(alarmValue,warnValue);
    }

    @Override
    public List<DfQmsIpqcWaigDetail> listSmallAreaInfo(String project) {
        return dfQmsIpqcWaigDetailMapper.listSmallAreaInfo(project);
    }

    @Override
    public List<DfQmsIpqcWaigDetail> listProcessInfo(String project) {
        return dfQmsIpqcWaigDetailMapper.listProcessInfo(project);
    }
}
