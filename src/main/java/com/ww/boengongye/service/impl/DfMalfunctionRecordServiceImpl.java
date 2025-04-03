package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfMalfunctionRecord;
import com.ww.boengongye.mapper.DfMalfunctionRecordMapper;
import com.ww.boengongye.service.DfMalfunctionRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备故障记录 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Service
public class DfMalfunctionRecordServiceImpl extends ServiceImpl<DfMalfunctionRecordMapper, DfMalfunctionRecord> implements DfMalfunctionRecordService {

    @Autowired
    DfMalfunctionRecordMapper DfMalfunctionRecordMapper;


    @Override
    public IPage<DfMalfunctionRecord> listJoinIds(IPage<DfMalfunctionRecord> page, Wrapper<DfMalfunctionRecord> wrapper) {
        return DfMalfunctionRecordMapper.listJoinIds(page, wrapper);
    }
}
