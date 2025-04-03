package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfRiskProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 风险品 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-10-25
 */
public interface DfRiskProductMapper extends BaseMapper<DfRiskProduct> {

    @Select("select drp.*,dqiwt.status as check_result\n" +
            "from df_risk_product drp \n" +
            "left join df_qms_ipqc_waig_total dqiwt \n" +
            "on drp.parent_id = dqiwt.id \n" +
            " ${ew.customSqlSegment} ")
    IPage<DfRiskProduct> getDfRiskProductList(IPage<DfRiskProduct> page,@Param(Constants.WRAPPER) Wrapper<DfRiskProduct> wrapper);
    @Select("select drp.*,dqiwt.status as check_result\n" +
            "from df_risk_product drp \n" +
            "left join df_qms_ipqc_waig_total dqiwt \n" +
            "on drp.parent_id = dqiwt.id \n" +
            " ${ew.customSqlSegment} ")
    List<DfRiskProduct> ListDfRiskProduct(@Param(Constants.WRAPPER) Wrapper<DfRiskProduct> wrapper);

}
