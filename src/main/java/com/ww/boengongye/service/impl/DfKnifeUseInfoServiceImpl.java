package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfKnifeUseInfo;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfKnifeUseInfoMapper;
import com.ww.boengongye.service.DfKnifeUseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 刀具使用信息 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-11-13
 */
@Service
public class DfKnifeUseInfoServiceImpl extends ServiceImpl<DfKnifeUseInfoMapper, DfKnifeUseInfo> implements DfKnifeUseInfoService {

    @Autowired
    private DfKnifeUseInfoMapper dfKnifeUseInfoMapper;

    @Override
    public List<Rate3> getKnifeLifeDistribution(Wrapper<DfKnifeUseInfo> wrapper) {
        return dfKnifeUseInfoMapper.getKnifeLifeDistribution(wrapper);
    }
}
