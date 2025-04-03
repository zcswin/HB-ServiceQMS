package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiMachineInspection;
import com.ww.boengongye.entity.DfAoiMachineProtect;
import com.ww.boengongye.entity.DfAoiPassPoint;
import com.ww.boengongye.mapper.DfAoiMachineProtectMapper;
import com.ww.boengongye.service.DfAoiMachineProtectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * AOI机台维护 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
@Service
public class DfAoiMachineProtectServiceImpl extends ServiceImpl<DfAoiMachineProtectMapper, DfAoiMachineProtect> implements DfAoiMachineProtectService {

    @Autowired
    DfAoiMachineProtectMapper dfAoiMachineProtectMapper;

    @Override
    public IPage<DfAoiMachineProtect> listJoinPage(IPage<DfAoiMachineProtect> page, Wrapper<DfAoiMachineProtect> wrapper) {
        return dfAoiMachineProtectMapper.listJoinPage(page,wrapper);
    }

    @Override
    public List<DfAoiMachineInspection> getAllMachineInspectionList(Wrapper<DfAoiMachineInspection> wrapper) {
        return dfAoiMachineProtectMapper.getAllMachineInspectionList(wrapper);
    }


    @Override
    public List<DfAoiMachineInspection> getMachineAndUserNameList(IPage<DfAoiMachineInspection> page,Wrapper<DfAoiMachineInspection> wrapper) {
        return dfAoiMachineProtectMapper.getMachineAndUserNameList(page,wrapper);
    }

    @Override
    public List<DfAoiMachineInspection> getMachineInspectionList(Wrapper<DfAoiMachineInspection> wrapper, Wrapper<DfAoiMachineInspection> wrapper2) {
        return dfAoiMachineProtectMapper.getMachineInspectionList(wrapper,wrapper2);
    }


    @Override
    public List<DfAoiPassPoint> getAoiPassPointOneList(Wrapper<DfAoiPassPoint> wrapper) {
        return dfAoiMachineProtectMapper.getAoiPassPointOneList(wrapper);
    }

    @Override
    public Integer getTotalInputNumber(Wrapper<Integer> wrapper) {
        return dfAoiMachineProtectMapper.getTotalInputNumber(wrapper);
    }

    @Override
    public Integer getBackNumber(Wrapper<Integer> wrapper) {
        return dfAoiMachineProtectMapper.getBackNumber(wrapper);
    }

    @Override
    public Integer getBackOKNumber(Wrapper<Integer> wrapper) {
        return dfAoiMachineProtectMapper.getBackOKNumber(wrapper);
    }

    @Override
    public List<DfAoiPassPoint> getAllAoiPassPointList(Wrapper<DfAoiPassPoint> wrapper) {
        return dfAoiMachineProtectMapper.getAllAoiPassPointList(wrapper);
    }

    @Override
    public List<DfAoiPassPoint> getAoiPassPointList(IPage<DfAoiPassPoint> page,Wrapper<DfAoiPassPoint> wrapper) {
        return dfAoiMachineProtectMapper.getAoiPassPointList(page,wrapper);
    }

    @Override
    public List<DfAoiPassPoint> getAoiPassPointWeekOneList(Wrapper<DfAoiPassPoint> wrapper) {
        return dfAoiMachineProtectMapper.getAoiPassPointWeekOneList(wrapper);
    }

    @Override
    public List<DfAoiPassPoint> getAoiPassPointWeekList(Wrapper<DfAoiPassPoint> wrapper) {
        return dfAoiMachineProtectMapper.getAoiPassPointWeekList(wrapper);
    }
}
