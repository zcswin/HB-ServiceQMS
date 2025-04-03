package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFaiPassRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeNgRate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 首检通过率 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
public interface DfFaiPassRateMapper extends BaseMapper<DfFaiPassRate> {

    @Select("select avg(all_mac_num) as all_mac_num, " +
            "sum(open_mac_num) as open_mac_num, " +
            "sum(fai_open_num) as fai_open_num, " +
            "sum(fai_pass_num) as fai_pass_num " +
            "from df_fai_pass_rate " +
            "${ew.customSqlSegment}")
    DfFaiPassRate getRate(@Param(Constants.WRAPPER) Wrapper<DfFaiPassRate> wrapper);

}
