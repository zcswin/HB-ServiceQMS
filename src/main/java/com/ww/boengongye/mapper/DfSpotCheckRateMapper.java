package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardStatus;
import com.ww.boengongye.entity.DfSpotCheckRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 点检率表 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-10-14
 */
public interface DfSpotCheckRateMapper extends BaseMapper<DfSpotCheckRate> {

    @Select("SELECT spo.*, fac.factory_name FROM df_spot_check_rate spo left join df_factory fac on spo.factory_id = fac.id " +
            "${ew.customSqlSegment}")
    IPage<DfSpotCheckRate> pageJoinFactory(IPage<DfSpotCheckRate> page, @Param(Constants.WRAPPER) Wrapper<DfSpotCheckRate> wrapper);
}
