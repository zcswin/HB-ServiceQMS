package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfRiskProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 风险品 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-10-25
 */
public interface DfRiskProductService extends IService<DfRiskProduct> {
    IPage<DfRiskProduct> getDfRiskProductList(IPage<DfRiskProduct> page, @Param(Constants.WRAPPER) Wrapper<DfRiskProduct> wrapper);
    List<DfRiskProduct> ListDfRiskProduct(@Param(Constants.WRAPPER) Wrapper<DfRiskProduct> wrapper);
}
