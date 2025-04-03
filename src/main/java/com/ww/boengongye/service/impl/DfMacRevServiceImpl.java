package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfMacRev;
import com.ww.boengongye.mapper.DfMacRevMapper;
import com.ww.boengongye.service.DfMacRevService;
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
public class DfMacRevServiceImpl extends ServiceImpl<DfMacRevMapper, DfMacRev> implements DfMacRevService {

    @Autowired
    private DfMacRevMapper dfMacRevMapper;

    @Override
    public List<Integer> deleteTimeOut() {
        return dfMacRevMapper.deleteTimeOut();
    }
}
