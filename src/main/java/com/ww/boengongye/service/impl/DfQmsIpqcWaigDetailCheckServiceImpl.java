package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfQmsIpqcWaigDetailCheckMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.service.DfQmsIpqcWaigDetailCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */
@Service
public class DfQmsIpqcWaigDetailCheckServiceImpl extends ServiceImpl<DfQmsIpqcWaigDetailCheckMapper, DfQmsIpqcWaigDetailCheck> implements DfQmsIpqcWaigDetailCheckService {

    @Autowired
    DfQmsIpqcWaigDetailCheckMapper dfQmsIpqcWaigDetailCheckMapper;

    @Override
    public List<DfQmsIpqcWaigDetailCheck> listByJoin(Wrapper<DfQmsIpqcWaigDetailCheck> wrapper) {
        return dfQmsIpqcWaigDetailCheckMapper.listByJoin(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigDetailCheck> listBySmAreaCount(Wrapper<DfQmsIpqcWaigDetailCheck> wrapper) {
        return dfQmsIpqcWaigDetailCheckMapper.listBySmAreaCount(wrapper);
    }

    @Override
    public DfQmsIpqcWaigDetailCheck getSumAffectCount(Wrapper<DfQmsIpqcWaigDetailCheck> wrapper) {
        return dfQmsIpqcWaigDetailCheckMapper.getSumAffectCount(wrapper);
    }

    @Override
    public List<DfSizeCheckItemInfos> fullApperanceNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime) {
        return dfQmsIpqcWaigDetailCheckMapper.fullApperanceNGTop5(factoryId,process,lineBody,item,startTime,endTime);
    }

    @Override
    public List<Rate3> getFpy(Wrapper<Rate3> wrapper, Wrapper<Rate3> wrapper2) {
        return dfQmsIpqcWaigDetailCheckMapper.getFpy(wrapper,wrapper2);
    }

    @Override
    public List<Rate3> getAfterFpy(Wrapper<Rate3> wrapper, Wrapper<Rate3> wrapper2) {
        return dfQmsIpqcWaigDetailCheckMapper.getAfterFpy(wrapper,wrapper2);
    }

    @Override
    public List<Rate4> getAlarmMessage(Double alarmValue) {
        return dfQmsIpqcWaigDetailCheckMapper.getAlarmMessage(alarmValue);
    }

    @Override
    public List<Rate4> getWarnMessage(Double alarmValue,Double warnValue) {
        return dfQmsIpqcWaigDetailCheckMapper.getWarnMessage(alarmValue,warnValue);
    }

    @Override
    public List<DfQmsIpqcWaigDetailCheck> listSmallAreaInfo(String project) {
        return dfQmsIpqcWaigDetailCheckMapper.listSmallAreaInfo(project);
    }

    @Override
    public List<DfQmsIpqcWaigDetailCheck> listProcessInfo(String project) {
        return dfQmsIpqcWaigDetailCheckMapper.listProcessInfo(project);
    }
}
