package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.ww.boengongye.entity.DfMacYieldData;
import com.ww.boengongye.mapper.DfMacStatusDetailMapper;
import com.ww.boengongye.service.DfMacStatusDetailService;
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
 * @since 2022-08-04
 */
@Service
public class DfMacStatusDetailServiceImpl extends ServiceImpl<DfMacStatusDetailMapper, DfMacStatusDetail> implements DfMacStatusDetailService {

    @Autowired
    private DfMacStatusDetailMapper dfMacStatusDetailMapper;

    @Override
    public List<Integer> deleteTimeOut() {
        return dfMacStatusDetailMapper.deleteTimeOut();
    }

    @Override
    public List<DfMacStatusDetail> listNormalStatus() {
        return dfMacStatusDetailMapper.listNormalStatus();
    }

    @Override
    public List<DfMacStatusDetail> listWarningStatus() {
        return dfMacStatusDetailMapper.listWarningStatus();
    }

    @Override
    public List<DfMacStatusDetail> listInsertMac(Wrapper<DfMacStatusDetail> wrapper) {
        return dfMacStatusDetailMapper.listInsertMac(wrapper);
    }

    @Override
    public List<DfMacStatusDetail> listJoinCode(Wrapper<DfMacStatusDetail> wrapper) {
        return dfMacStatusDetailMapper.listJoinCode(wrapper);
    }
}
