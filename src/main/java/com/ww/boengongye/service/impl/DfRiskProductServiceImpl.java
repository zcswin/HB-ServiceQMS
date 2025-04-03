package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfRiskProduct;
import com.ww.boengongye.mapper.DfRiskProductMapper;
import com.ww.boengongye.service.DfRiskProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 风险品 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-10-25
 */
@Service
public class DfRiskProductServiceImpl extends ServiceImpl<DfRiskProductMapper, DfRiskProduct> implements DfRiskProductService {

    @Autowired
    private DfRiskProductMapper dfRiskProductMapper;

    @Override
    public IPage<DfRiskProduct> getDfRiskProductList(IPage<DfRiskProduct> page, Wrapper<DfRiskProduct> wrapper) {
        return dfRiskProductMapper.getDfRiskProductList(page,wrapper);
    }

    @Override
    public List<DfRiskProduct> ListDfRiskProduct(Wrapper<DfRiskProduct> wrapper) {
        return dfRiskProductMapper.ListDfRiskProduct(wrapper);
    }
}
