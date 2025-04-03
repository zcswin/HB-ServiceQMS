package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfProcess;
import com.ww.boengongye.mapper.DfProcessMapper;
import com.ww.boengongye.service.DfProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工序 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-19
 */
@Service
public class DfProcessServiceImpl extends ServiceImpl<DfProcessMapper, DfProcess> implements DfProcessService {
    @Autowired
    DfProcessMapper DfProcessMapper;

    @Override
    public List<DfProcess> listByRouting(int id) {
        return DfProcessMapper.listByRouting(id);
    }

    @Override
    public IPage<DfProcess> listJoinIds(IPage<DfProcess> page, Wrapper<DfProcess> wrapper) {
        return DfProcessMapper.listJoinIds(page, wrapper);
    }

    @Override
    public List<DfProcess> listMacProcessStatus() {
        return DfProcessMapper.listMacProcessStatus();
    }

    @Override
    public List<DfProcess> listDfProcess( Wrapper<DfProcess> wrapper) {
        return DfProcessMapper.listDfProcess(wrapper);
    }

}
