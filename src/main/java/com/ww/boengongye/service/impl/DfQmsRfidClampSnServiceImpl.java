package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfQmsRfidClampSn;
import com.ww.boengongye.mapper.DfQmsRfidClampSnMapper;
import com.ww.boengongye.service.DfQmsRfidClampSnService;
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
 * @since 2024-06-24
 */
@Service
public class DfQmsRfidClampSnServiceImpl extends ServiceImpl<DfQmsRfidClampSnMapper, DfQmsRfidClampSn> implements DfQmsRfidClampSnService {

    @Autowired
    DfQmsRfidClampSnMapper DfQmsRfidClampSnMapper;
    @Override
    public List<DfQmsRfidClampSn> listByExport(Wrapper<DfQmsRfidClampSn> wrapper) {
        return DfQmsRfidClampSnMapper.listByExport(wrapper);
    }
}
