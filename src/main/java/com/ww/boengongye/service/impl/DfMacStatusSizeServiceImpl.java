package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfMacStatusSizeMapper;
import com.ww.boengongye.service.DfMacStatusSizeService;
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
 * @since 2023-03-11
 */
@Service
public class DfMacStatusSizeServiceImpl extends ServiceImpl<DfMacStatusSizeMapper, DfMacStatusSize> implements DfMacStatusSizeService {

    @Autowired
    DfMacStatusSizeMapper DfMacStatusSizeMapper;


    @Override
    public List<DfMacStatusSize> listStatus() {
        return DfMacStatusSizeMapper.listStatus();
    }

    @Override
    public List<DfMacStatusSize> listStatus2(Wrapper<DfMacStatusSize> wrapper) {
        return DfMacStatusSizeMapper.listStatus2(wrapper);
    }


    @Override
    public List<DfMacStatusSize> countByStatus() {
        return DfMacStatusSizeMapper.countByStatus();
    }

    @Override
    public List<DfMacStatusSize> listJoinCode(Wrapper<DfMacStatusSize> wrapper) {
        return DfMacStatusSizeMapper.listJoinCode(wrapper);
    }

    @Override
    public List<Rate3> getProcessMacStatusList(Wrapper<DfMacStatusSize> wrapper) {
        return DfMacStatusSizeMapper.getProcessMacStatusList(wrapper);
    }

    @Override
    public List<Rate3> getMacStatusInfoList(Wrapper<DfSizeDetail> wrapper) {
        return DfMacStatusSizeMapper.getMacStatusInfoList(wrapper);
    }

    @Override
    public List<Rate3> getProcessMacNormalTime(Wrapper<DfSizeMacDuration> wrapper) {
        return DfMacStatusSizeMapper.getProcessMacNormalTime(wrapper);
    }

    @Override
    public List<Rate3> countSizeMacDurationInfoList(Wrapper<DfSizeMacDuration> wrapper1, Wrapper<DfSizeDetail> wrapper2) {
        return DfMacStatusSizeMapper.countSizeMacDurationInfoList(wrapper1,wrapper2);
    }

}
