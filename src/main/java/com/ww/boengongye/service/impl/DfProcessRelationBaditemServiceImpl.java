package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfBadItem;
import com.ww.boengongye.entity.DfProcessRelationBaditem;
import com.ww.boengongye.mapper.DfProcessRelationBaditemMapper;
import com.ww.boengongye.service.DfProcessRelationBaditemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工序_不良项配置 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
@Service
public class DfProcessRelationBaditemServiceImpl extends ServiceImpl<DfProcessRelationBaditemMapper, DfProcessRelationBaditem> implements DfProcessRelationBaditemService {

    @Autowired
    private DfProcessRelationBaditemMapper dfProcessRelationBaditemMapper;

    @Override
    public List<DfBadItem> listSelectedBadItem(Integer processId) {
        return dfProcessRelationBaditemMapper.listSelectedBadItem(processId);
    }

    @Override
    public List<DfBadItem> listUnselectedBadItem(Integer processId) {
        return dfProcessRelationBaditemMapper.listUnselectedBadItem(processId);

    }
}
